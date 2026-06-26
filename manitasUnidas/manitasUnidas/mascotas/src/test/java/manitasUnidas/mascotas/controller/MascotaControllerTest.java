package manitasUnidas.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import manitasUnidas.mascotas.dto.ApiResponse;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.dto.MascotaResponseDTO;
import manitasUnidas.mascotas.exception.ResourceNotFoundException;
import manitasUnidas.mascotas.service.MascotaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitario para MascotaController.
 *
 * QUÉ SE PRUEBA AQUÍ:
 * - Solo la capa Controller (lo que recibe y lo que responde).
 * - Que los endpoints respondan los códigos HTTP correctos.
 * - Que el JSON de respuesta tenga los campos esperados.
 *
 * QUÉ NO SE PRUEBA AQUÍ:
 * - La lógica de negocio (eso lo prueba MascotaServiceTest).
 * - La base de datos (no se conecta a MySQL).
 * - Eureka ni otros microservicios.
 *
 * POR QUÉ @WebMvcTest:
 * Levanta SOLO el controller en un contexto mínimo de Spring.
 * No levanta toda la aplicación, no necesita MySQL ni Eureka.
 * Es rápido y enfocado.
 *
 * POR QUÉ @MockitoBean en MascotaService:
 * El controller depende del service, pero aquí NO queremos
 * probar el service. Lo simulamos con Mockito para controlar
 * exactamente qué devuelve en cada test.
 */
@WebMvcTest(MascotaController.class)
@DisplayName("Tests de Controller - MascotaController")
class MascotaControllerTest {

    // MockMvc simula peticiones HTTP sin levantar un servidor real.
    // Es como un "cliente de prueba" que llama a los endpoints.
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper convierte objetos Java a JSON y viceversa.
    // Lo usamos para convertir el DTO de request a JSON en los POST/PUT.
    @Autowired
    private ObjectMapper objectMapper;

    // @MockitoBean reemplaza el MascotaService real por uno simulado.
    // Así el controller funciona, pero el service no hace nada real.
    @MockitoBean
    private MascotaService mascotaService;

    // Datos de prueba reutilizables en todos los tests.
    private MascotaResponseDTO mascotaRespuesta;
    private MascotaRequestDTO mascotaRequest;

    @BeforeEach
    void setUp() {
        // Creamos una mascota de respuesta simulada
        mascotaRespuesta = new MascotaResponseDTO();
        mascotaRespuesta.setId(1L);
        mascotaRespuesta.setNombre("Luna");
        mascotaRespuesta.setEspecie("Perro");
        mascotaRespuesta.setRaza("Labrador");
        mascotaRespuesta.setEdad(3);
        mascotaRespuesta.setSexo("Hembra");
        mascotaRespuesta.setDescripcion("Muy amigable");
        mascotaRespuesta.setEstado("Disponible");
        mascotaRespuesta.setDuenoId(1L);
        mascotaRespuesta.setRefugioId(1L);

        // Creamos un request DTO para POST y PUT
        mascotaRequest = new MascotaRequestDTO();
        mascotaRequest.setNombre("Luna");
        mascotaRequest.setEspecie("Perro");
        mascotaRequest.setRaza("Labrador");
        mascotaRequest.setEdad(3);
        mascotaRequest.setSexo("Hembra");
        mascotaRequest.setDescripcion("Muy amigable");
        mascotaRequest.setEstado("Disponible");
        mascotaRequest.setDuenoId(1L);
        mascotaRequest.setRefugioId(1L);
    }

    // =====================================================================
    // TEST 1: GET /api/mascotas
    // =====================================================================

    @Test
    @DisplayName("GET /api/mascotas - debe retornar 200 OK con lista de mascotas")
    void listar_debeRetornar200ConLista() throws Exception {

        /*
         * ARRANGE:
         * Le decimos al service simulado que cuando se llame a obtenerTodas(),
         * devuelva una lista con una mascota de prueba.
         * Así controlamos exactamente qué datos recibe el controller.
         */
        when(mascotaService.obtenerTodas()).thenReturn(List.of(mascotaRespuesta));

        /*
         * ACT + ASSERT:
         * MockMvc simula una petición GET a /api/mascotas.
         * Verificamos que la respuesta sea 200 OK
         * y que el JSON contenga los datos esperados.
         *
         * Por qué $.data._embedded.mascotaResponseDTOList[0].nombre:
         * Nuestro controller envuelve la respuesta en ApiResponse y CollectionModel
         * (por HATEOAS), por eso el JSON tiene esa estructura anidada.
         */
        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.mensaje").value("Mascotas obtenidas correctamente"))
                .andExpect(jsonPath("$.data._embedded.mascotaResponseDTOList[0].nombre")
                        .value("Luna"))
                .andExpect(jsonPath("$.data._embedded.mascotaResponseDTOList[0].estado")
                        .value("Disponible"));

        /*
         * VERIFY:
         * Comprobamos que el controller efectivamente llamó al service.
         * Si el controller ignorara el service, este verify fallaría.
         */
        verify(mascotaService, times(1)).obtenerTodas();

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si este test falla con "Expected: 200 but was: 500",
         * significa que el controller tiene un error interno.
         * Desarrollo debe revisar MascotaController.listar()
         * y verificar que MascotaService.obtenerTodas() no lanza excepciones.
         */
    }

    // =====================================================================
    // TEST 2: GET /api/mascotas/{id} — mascota existente
    // =====================================================================

    @Test
    @DisplayName("GET /api/mascotas/1 - debe retornar 200 OK con la mascota encontrada")
    void buscarPorId_cuandoExiste_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         * Cuando el controller llame a buscarPorId(1L),
         * el service simulado devuelve nuestra mascota de prueba.
         */
        when(mascotaService.buscarPorId(1L)).thenReturn(mascotaRespuesta);

        /*
         * ACT + ASSERT:
         * Simulamos GET /api/mascotas/1 y verificamos que:
         * - La respuesta sea 200 OK.
         * - El JSON contenga el ID y nombre correctos.
         *
         * $.data.id: el controller envuelve en ApiResponse ($.data)
         * y luego en EntityModel de HATEOAS, por eso accedemos con $.data.id
         * (HATEOAS aplana los campos del DTO en el nivel data).
         */
        mockMvc.perform(get("/api/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nombre").value("Luna"))
                .andExpect(jsonPath("$.data.especie").value("Perro"))
                .andExpect(jsonPath("$.data.estado").value("Disponible"));

        /*
         * VERIFY:
         */
        verify(mascotaService, times(1)).buscarPorId(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: Luna but was: null",
         * el controller no está mapeando bien el DTO de respuesta.
         * Desarrollo debe revisar el método toDTO() en MascotaService.
         */
    }

    // =====================================================================
    // TEST 3: GET /api/mascotas/{id} — mascota NO existe (404)
    // =====================================================================

    @Test
    @DisplayName("GET /api/mascotas/99 - debe retornar 404 cuando no existe")
    void buscarPorId_cuandoNoExiste_debeRetornar404() throws Exception {

        /*
         * ARRANGE:
         * Cuando se busque el ID 99, el service lanza ResourceNotFoundException.
         * Esto simula el caso real cuando una mascota no existe en la BD.
         */
        when(mascotaService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Mascota no encontrada id=99"));

        /*
         * ACT + ASSERT:
         * Verificamos que el endpoint responda 404 Not Found.
         * El GlobalExceptionHandler captura la excepción y devuelve 404.
         */
        mockMvc.perform(get("/api/mascotas/99"))
                .andExpect(status().isNotFound());

        /*
         * VERIFY:
         */
        verify(mascotaService, times(1)).buscarPorId(99L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si este test falla con "Expected: 404 but was: 200",
         * significa que el GlobalExceptionHandler no está capturando
         * ResourceNotFoundException correctamente.
         * Desarrollo debe revisar GlobalExceptionHandler en ms-mascotas.
         */
    }

    // =====================================================================
    // TEST 4: POST /api/mascotas — crear mascota exitosamente
    // =====================================================================

    @Test
    @DisplayName("POST /api/mascotas - debe retornar 201 CREATED con la mascota creada")
    void guardar_cuandoDatosValidos_debeRetornar201() throws Exception {

        /*
         * ARRANGE:
         * Cuando el controller llame a registrarMascota() con cualquier DTO,
         * el service simulado devuelve la mascota de respuesta.
         *
         * Por qué any(MascotaRequestDTO.class):
         * No nos importa el objeto exacto que llega al service,
         * solo que se llame al método y devuelva la respuesta.
         */
        when(mascotaService.registrarMascota(any(MascotaRequestDTO.class)))
                .thenReturn(mascotaRespuesta);

        /*
         * ACT + ASSERT:
         * Simulamos un POST con el JSON del request.
         * objectMapper.writeValueAsString() convierte el DTO a JSON.
         * Verificamos que responda 201 CREATED.
         */
        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.mensaje").value("Mascota registrada correctamente"))
                .andExpect(jsonPath("$.data.nombre").value("Luna"));

        /*
         * VERIFY:
         */
        verify(mascotaService, times(1)).registrarMascota(any(MascotaRequestDTO.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 201 but was: 400",
         * algún campo del request no pasa las validaciones @NotBlank/@NotNull.
         * Desarrollo debe revisar qué campo falta en el MascotaRequestDTO.
         */
    }

    // =====================================================================
    // TEST 5: POST /api/mascotas — datos inválidos (400)
    // =====================================================================

    @Test
    @DisplayName("POST /api/mascotas - debe retornar 400 cuando faltan datos obligatorios")
    void guardar_cuandoDatosInvalidos_debeRetornar400() throws Exception {

        /*
         * ARRANGE:
         * Creamos un request SIN nombre ni especie (campos @NotBlank).
         * El controller debería rechazar esto antes de llamar al service.
         */
        MascotaRequestDTO requestInvalido = new MascotaRequestDTO();
        // Dejamos nombre y especie vacíos — son @NotBlank

        /*
         * ACT + ASSERT:
         * Spring Validation rechaza el request con 400 Bad Request
         * antes de que llegue al service.
         */
        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        /*
         * VERIFY:
         * El service NO debe ser llamado porque la validación fallió antes.
         */
        verify(mascotaService, never()).registrarMascota(any());

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 400 but was: 201",
         * las anotaciones de validación (@NotBlank, @NotNull) no están
         * funcionando. Desarrollo debe verificar que el pom.xml tenga
         * spring-boot-starter-validation y que el controller use @Valid.
         */
    }

    // =====================================================================
    // TEST 6: PUT /api/mascotas/{id} — actualizar exitosamente
    // =====================================================================

    @Test
    @DisplayName("PUT /api/mascotas/1 - debe retornar 200 OK con mascota actualizada")
    void actualizar_cuandoExiste_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         */
        when(mascotaService.actualizarMascota(eq(1L), any(MascotaRequestDTO.class)))
                .thenReturn(mascotaRespuesta);

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(put("/api/mascotas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.nombre").value("Luna"));

        /*
         * VERIFY:
         */
        verify(mascotaService, times(1)).actualizarMascota(eq(1L), any(MascotaRequestDTO.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 404",
         * el ID no existe en la BD o el service lanza ResourceNotFoundException.
         * Desarrollo debe verificar que la mascota existe antes de actualizar.
         */
    }

    // =====================================================================
    // TEST 7: DELETE /api/mascotas/{id} — eliminar exitosamente
    // =====================================================================

    @Test
    @DisplayName("DELETE /api/mascotas/1 - debe retornar 200 OK al eliminar")
    void eliminar_cuandoExiste_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         * doNothing() significa que cuando se llame a eliminar(1L),
         * el service no hace nada (void method simulado).
         */
        doNothing().when(mascotaService).eliminar(1L);

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(delete("/api/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.mensaje").value("Mascota eliminada correctamente"));

        /*
         * VERIFY:
         */
        verify(mascotaService, times(1)).eliminar(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 404",
         * el ID no existe. Desarrollo debe verificar el flujo de eliminación
         * en MascotaService.eliminar().
         */
    }
}