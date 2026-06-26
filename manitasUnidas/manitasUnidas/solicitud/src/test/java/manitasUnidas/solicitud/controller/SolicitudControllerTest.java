package manitasUnidas.solicitud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import manitasUnidas.solicitud.dto.SolicitudRequestDTO;
import manitasUnidas.solicitud.exception.ResourceNotFoundException;
import manitasUnidas.solicitud.exception.SolicitudRechazadaException;
import manitasUnidas.solicitud.model.Solicitud;
import manitasUnidas.solicitud.service.SolicitudService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitario para SolicitudController.
 *
 * QUÉ SE PRUEBA AQUÍ:
 * - Que los endpoints respondan los códigos HTTP correctos.
 * - Que el JSON de respuesta tenga los campos esperados.
 * - Casos de error: 404 cuando no existe, 403 cuando está en lista negra.
 *
 * QUÉ NO SE PRUEBA AQUÍ:
 * - Las validaciones de negocio (lista negra, disponibilidad) — eso es SolicitudServiceTest.
 * - La base de datos.
 * - Los otros microservicios (BlackList, Mascotas, Usuarios).
 *
 * POR QUÉ @WebMvcTest:
 * Solo levanta SolicitudController en un contexto mínimo.
 * SolicitudService queda simulado con @MockitoBean.
 */
@WebMvcTest(SolicitudController.class)
@DisplayName("Tests de Controller - SolicitudController")
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudService service;

    private Solicitud solicitudEjemplo;
    private SolicitudRequestDTO dtoRequest;

    @BeforeEach
    void setUp() {
        solicitudEjemplo = new Solicitud();
        solicitudEjemplo.setId(1L);
        solicitudEjemplo.setIdAdoptante(1L);
        solicitudEjemplo.setRutAdoptante("12345678-9");
        solicitudEjemplo.setIdMascota(1L);
        solicitudEjemplo.setEstado("PENDIENTE");
        solicitudEjemplo.setFechaSolicitud(LocalDate.now());
        solicitudEjemplo.setObservaciones("Quiero adoptar");

        dtoRequest = new SolicitudRequestDTO(1L, "12345678-9", 1L, "Quiero adoptar");
    }

    // =====================================================================
    // TEST 1: GET /api/solicitudes — listar todas
    // =====================================================================

    @Test
    @DisplayName("GET /api/solicitudes - debe retornar 200 OK con lista de solicitudes")
    void listar_debeRetornar200ConLista() throws Exception {

        /*
         * ARRANGE:
         * El service simulado devuelve una lista con una solicitud de prueba.
         */
        when(service.obtenerTodas()).thenReturn(List.of(solicitudEjemplo));

        /*
         * ACT + ASSERT:
         * El controller retorna CollectionModel (HATEOAS), por eso el JSON
         * tiene la estructura _embedded.solicitudList[0].
         */
        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.solicitudList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.solicitudList[0].estado").value("PENDIENTE"))
                .andExpect(jsonPath("$._embedded.solicitudList[0].idAdoptante").value(1L));

        /*
         * VERIFY:
         */
        verify(service, times(1)).obtenerTodas();

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 500",
         * hay un error en SolicitudController.listar().
         * Si falla con "$.estado Expected: PENDIENTE but was: null",
         * el campo estado no se está serializando correctamente en el JSON.
         */
    }

    // =====================================================================
    // TEST 2: GET /api/solicitudes/{id} — solicitud existente
    // =====================================================================

    @Test
    @DisplayName("GET /api/solicitudes/1 - debe retornar 200 OK con la solicitud")
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         */
        when(service.buscarPorId(1L)).thenReturn(solicitudEjemplo);

        /*
         * ACT + ASSERT:
         * El controller retorna EntityModel (HATEOAS), los campos
         * del objeto quedan en el nivel raíz del JSON.
         */
        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.rutAdoptante").value("12345678-9"));

        /*
         * VERIFY:
         */
        verify(service, times(1)).buscarPorId(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 404",
         * la solicitud no existe o el ID está mal.
         */
    }

    // =====================================================================
    // TEST 3: GET /api/solicitudes/{id} — solicitud NO existe (404)
    // =====================================================================

    @Test
    @DisplayName("GET /api/solicitudes/99 - debe retornar 404 cuando no existe")
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {

        /*
         * ARRANGE:
         * El service lanza ResourceNotFoundException para el ID 99.
         */
        when(service.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Solicitud no encontrada con ID: 99"));

        /*
         * ACT + ASSERT:
         * El GlobalExceptionHandler captura la excepción y responde 404.
         */
        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound());

        /*
         * VERIFY:
         */
        verify(service, times(1)).buscarPorId(99L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 404 but was: 200",
         * el GlobalExceptionHandler no captura ResourceNotFoundException.
         * Desarrollo debe verificar la clase GlobalExceptionHandler en ms-solicitud.
         */
    }

    // =====================================================================
    // TEST 4: POST /api/solicitudes — crear exitosamente
    // =====================================================================

    @Test
    @DisplayName("POST /api/solicitudes - debe retornar 201 CREATED con la solicitud creada")
    void crear_cuandoDatosValidos_debeRetornar201() throws Exception {

        /*
         * ARRANGE:
         * Cuando se llame a crearSolicitud() con cualquier DTO,
         * el service devuelve la solicitud de prueba.
         */
        when(service.crearSolicitud(any(SolicitudRequestDTO.class)))
                .thenReturn(solicitudEjemplo);

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.idAdoptante").value(1L));

        /*
         * VERIFY:
         */
        verify(service, times(1)).crearSolicitud(any(SolicitudRequestDTO.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 201 but was: 403",
         * el service está lanzando SolicitudRechazadaException.
         * Esto significa que el adoptante está en lista negra o la mascota no está disponible.
         * Desarrollo debe revisar los datos de prueba y las validaciones en SolicitudService.
         */
    }

    // =====================================================================
    // TEST 5: POST /api/solicitudes — adoptante en lista negra (403)
    // =====================================================================

    @Test
    @DisplayName("POST /api/solicitudes - debe retornar 403 cuando adoptante está en lista negra")
    void crear_cuandoAdoptanteBloqueado_debeRetornar403() throws Exception {

        /*
         * ARRANGE:
         * El service lanza SolicitudRechazadaException porque el adoptante
         * está en la lista negra.
         *
         * POR QUÉ ESTE TEST ES IMPORTANTE:
         * Es una de las reglas de negocio centrales del sistema.
         * Si el controller no responde 403 en este caso, el sistema
         * permitiría adopciones a adoptantes bloqueados.
         */
        when(service.crearSolicitud(any(SolicitudRequestDTO.class)))
                .thenThrow(new SolicitudRechazadaException(
                        "El adoptante con RUT 12345678-9 está en la lista negra."));

        /*
         * ACT + ASSERT:
         * GlobalExceptionHandler captura SolicitudRechazadaException y responde 403.
         */
        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isForbidden());

        /*
         * VERIFY:
         */
        verify(service, times(1)).crearSolicitud(any(SolicitudRequestDTO.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 403 but was: 201",
         * el GlobalExceptionHandler no está capturando SolicitudRechazadaException,
         * o está respondiendo con un código incorrecto.
         * Desarrollo debe verificar el @ExceptionHandler de SolicitudRechazadaException.
         */
    }

    // =====================================================================
    // TEST 6: PUT /api/solicitudes/{id}/estado — cambiar a APROBADA
    // =====================================================================

    @Test
    @DisplayName("PUT /api/solicitudes/1/estado - debe retornar 200 OK con estado actualizado")
    void actualizarEstado_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         * Preparamos una solicitud con estado APROBADA para la respuesta.
         */
        solicitudEjemplo.setEstado("APROBADA");
        when(service.cambiarEstado(eq(1L), any(String.class)))
                .thenReturn(solicitudEjemplo);

        /*
         * ACT + ASSERT:
         * Enviamos el nuevo estado como texto plano en el body.
         */
        mockMvc.perform(put("/api/solicitudes/1/estado")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("APROBADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        /*
         * VERIFY:
         */
        verify(service, times(1)).cambiarEstado(eq(1L), any(String.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: APROBADA but was: PENDIENTE",
         * el service no está actualizando el estado correctamente.
         * Desarrollo debe revisar SolicitudService.cambiarEstado().
         */
    }

    // =====================================================================
    // TEST 7: DELETE /api/solicitudes/{id} — eliminar exitosamente
    // =====================================================================

    @Test
    @DisplayName("DELETE /api/solicitudes/1 - debe retornar 204 No Content")
    void eliminar_cuandoExiste_debeRetornar204() throws Exception {

        /*
         * ARRANGE:
         * doNothing() porque eliminarSolicitud() es void.
         */
        doNothing().when(service).eliminarSolicitud(1L);

        /*
         * ACT + ASSERT:
         * El controller responde 204 No Content (sin body).
         */
        mockMvc.perform(delete("/api/solicitudes/1"))
                .andExpect(status().isNoContent());

        /*
         * VERIFY:
         */
        verify(service, times(1)).eliminarSolicitud(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 204 but was: 404",
         * la solicitud no existe. Desarrollo debe verificar
         * el flujo en SolicitudService.eliminarSolicitud().
         */
    }
}