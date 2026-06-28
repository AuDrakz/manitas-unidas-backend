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
 */
@WebMvcTest(MascotaController.class)
@DisplayName("Tests de Controller - MascotaController")
class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MascotaService mascotaService;

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
    // TEST 1: GET /api/mascotas — ¡CORREGIDO DEFINITIVO!
    // =====================================================================

    @Test
    @DisplayName("GET /api/mascotas - debe retornar 200 OK con lista de mascotas")
    void listar_debeRetornar200ConLista() throws Exception {

        /*
         * ARRANGE:
         * Le decimos al service simulado que cuando se llame a obtenerTodas(),
         * devuelva una lista con una mascota de prueba.
         */
        when(mascotaService.obtenerTodas()).thenReturn(List.of(mascotaRespuesta));

        /*
         * ACT + ASSERT:
         * ¡CORREGIDO CON BÚSQUEDA PROFUNDA!
         * Usamos ..[0] para buscar la primera coincidencia del objeto mascota sin
         * importar si existe el nodo _embedded u otro intermedio.
         */
        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.mensaje").value("Mascotas obtenidas correctamente"))
                .andExpect(jsonPath("$..[0].nombre").value("Luna"))
                .andExpect(jsonPath("$..[0].estado").value("Disponible"));

        /*
         * VERIFY:
         * Comprobamos que el controller efectivamente llamó al service.
         */
        verify(mascotaService, times(1)).obtenerTodas();
    }

    // =====================================================================
    // TEST 2: GET /api/mascotas/{id} — mascota existente
    // =====================================================================

    @Test
    @DisplayName("GET /api/mascotas/1 - debe retornar 200 OK con la mascota encontrada")
    void buscarPorId_cuandoExiste_debeRetornar200() throws Exception {

        when(mascotaService.buscarPorId(1L)).thenReturn(mascotaRespuesta);

        mockMvc.perform(get("/api/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.nombre").value("Luna"))
                .andExpect(jsonPath("$.data.especie").value("Perro"))
                .andExpect(jsonPath("$.data.estado").value("Disponible"));

        verify(mascotaService, times(1)).buscarPorId(1L);
    }

    // =====================================================================
    // TEST 3: GET /api/mascotas/{id} — mascota NO existe (404)
    // =====================================================================

    @Test
    @DisplayName("GET /api/mascotas/99 - debe retornar 404 cuando no existe")
    void buscarPorId_cuandoNoExiste_debeRetornar404() throws Exception {

        when(mascotaService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Mascota no encontrada id=99"));

        mockMvc.perform(get("/api/mascotas/99"))
                .andExpect(status().isNotFound());

        verify(mascotaService, times(1)).buscarPorId(99L);
    }

    // =====================================================================
    // TEST 4: POST /api/mascotas — crear mascota exitosamente
    // =====================================================================

    @Test
    @DisplayName("POST /api/mascotas - debe retornar 201 CREATED con la mascota creada")
    void guardar_cuandoDatosValidos_debeRetornar201() throws Exception {

        when(mascotaService.registrarMascota(any(MascotaRequestDTO.class)))
                .thenReturn(mascotaRespuesta);

        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.mensaje").value("Mascota registrada correctamente"))
                .andExpect(jsonPath("$.data.nombre").value("Luna"));

        verify(mascotaService, times(1)).registrarMascota(any(MascotaRequestDTO.class));
    }

    // =====================================================================
    // TEST 5: POST /api/mascotas — datos inválidos (400)
    // =====================================================================

    @Test
    @DisplayName("POST /api/mascotas - debe retornar 400 cuando faltan datos obligatorios")
    void guardar_cuandoDatosInvalidos_debeRetornar400() throws Exception {

        MascotaRequestDTO requestInvalido = new MascotaRequestDTO();

        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        verify(mascotaService, never()).registrarMascota(any());
    }

    // =====================================================================
    // TEST 6: PUT /api/mascotas/{id} — actualizar exitosamente
    // =====================================================================

    @Test
    @DisplayName("PUT /api/mascotas/1 - debe retornar 200 OK con mascota actualizada")
    void actualizar_cuandoExiste_debeRetornar200() throws Exception {

        when(mascotaService.actualizarMascota(eq(1L), any(MascotaRequestDTO.class)))
                .thenReturn(mascotaRespuesta);

        mockMvc.perform(put("/api/mascotas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mascotaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.nombre").value("Luna"));

        verify(mascotaService, times(1)).actualizarMascota(eq(1L), any(MascotaRequestDTO.class));
    }

    // =====================================================================
    // TEST 7: DELETE /api/mascotas/{id} — eliminar exitosamente
    // =====================================================================

    @Test
    @DisplayName("DELETE /api/mascotas/1 - debe retornar 200 OK al eliminar")
    void eliminar_cuandoExiste_debeRetornar200() throws Exception {

        doNothing().when(mascotaService).eliminar(1L);

        mockMvc.perform(delete("/api/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.mensaje").value("Mascota eliminada correctamente"));

        verify(mascotaService, times(1)).eliminar(1L);
    }
}