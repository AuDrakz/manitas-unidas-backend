package manitasUnidas.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import manitasUnidas.usuarios.exception.ResourceNotFoundException;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.service.UsuarioService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
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
 * Test unitario de Controller para ms-usuarios.
 *
 * QUÉ SE PRUEBA AQUÍ:
 * - Que los endpoints respondan los códigos HTTP correctos (200, 201, 204, 400, 404).
 * - Que el JSON de respuesta tenga los campos esperados.
 * - Que las validaciones @NotBlank y @Email rechacen datos inválidos con 400.
 *
 * QUÉ NO SE PRUEBA AQUÍ:
 * - La lógica de RUT duplicado (eso es UsuarioServiceTest).
 * - La base de datos.
 *
 * POR QUÉ @WebMvcTest:
 * Solo levanta UsuarioController. No conecta a MySQL ni Eureka.
 * UsuarioService queda reemplazado por un mock con @MockitoBean.
 */
@WebMvcTest(UsuarioController.class)
@DisplayName("Tests de Controller - UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Reemplaza el UsuarioService real por uno simulado.
    // Así el controller funciona sin necesitar la BD.
    @MockitoBean
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setRut("12345678-9");
        usuarioEjemplo.setNombre("Juan Perez");
        usuarioEjemplo.setCorreo("juan@correo.cl");
        usuarioEjemplo.setPassword("password123");
        usuarioEjemplo.setTelefono("912345678");
        usuarioEjemplo.setDireccion("Calle 123");
        usuarioEjemplo.setRol("Adoptante");
    }

    // =====================================================================
    // TEST 1: GET /api/usuarios — listar todos
    // =====================================================================

    @Test
    @DisplayName("GET /api/usuarios - debe retornar 200 OK con lista de usuarios")
    void listar_debeRetornar200ConLista() throws Exception {

        /*
         * ARRANGE:
         * El service simulado devuelve una lista con un usuario de prueba.
         */
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuarioEjemplo));

        /*
         * ACT + ASSERT:
         * El controller retorna CollectionModel (HATEOAS), por eso el JSON
         * tiene la estructura _embedded.usuarioList[0].
         */
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.usuarioList[0].nombre").value("Juan Perez"))
                .andExpect(jsonPath("$._embedded.usuarioList[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$._embedded.usuarioList[0].rol").value("Adoptante"));

        /*
         * VERIFY:
         * Confirmamos que el controller llamó al service exactamente una vez.
         */
        verify(usuarioService, times(1)).obtenerTodos();

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 500",
         * hay un error no controlado en UsuarioController.listar().
         * Si falla con "Expected: Juan Perez but was: null",
         * el campo nombre no se está serializando correctamente.
         * Desarrollo debe verificar que Usuario tenga @Data o getters.
         */
    }

    // =====================================================================
    // TEST 2: GET /api/usuarios/{id} — usuario existente
    // =====================================================================

    @Test
    @DisplayName("GET /api/usuarios/1 - debe retornar 200 OK con el usuario encontrado")
    void buscarPorId_cuandoExiste_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         * El service devuelve el usuario de prueba cuando se busca el ID 1.
         */
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioEjemplo);

        /*
         * ACT + ASSERT:
         * El controller retorna EntityModel (HATEOAS).
         * Los campos del usuario quedan en el nivel raíz del JSON.
         */
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$.correo").value("juan@correo.cl"))
                .andExpect(jsonPath("$.rol").value("Adoptante"));

        /*
         * VERIFY:
         */
        verify(usuarioService, times(1)).buscarPorId(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 404",
         * el usuario no existe o el mock no está configurado correctamente.
         */
    }

    // =====================================================================
    // TEST 3: GET /api/usuarios/{id} — usuario NO existe (404)
    // =====================================================================

    @Test
    @DisplayName("GET /api/usuarios/99 - debe retornar 404 cuando no existe")
    void buscarPorId_cuandoNoExiste_debeRetornar404() throws Exception {

        /*
         * ARRANGE:
         * El service lanza ResourceNotFoundException para el ID 99.
         * El GlobalExceptionHandler lo captura y responde 404.
         */
        when(usuarioService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado con ID: 99"));

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Usuario no encontrado"))
                .andExpect(jsonPath("$.mensaje").value("Usuario no encontrado con ID: 99"));

        /*
         * VERIFY:
         */
        verify(usuarioService, times(1)).buscarPorId(99L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 404 but was: 200",
         * el GlobalExceptionHandler no está capturando ResourceNotFoundException.
         * Desarrollo debe verificar GlobalExceptionHandler en ms-usuarios.
         */
    }

    // =====================================================================
    // TEST 4: POST /api/usuarios — crear usuario exitosamente
    // =====================================================================

    @Test
    @DisplayName("POST /api/usuarios - debe retornar 201 CREATED con el usuario creado")
    void guardar_cuandoDatosValidos_debeRetornar201() throws Exception {

        /*
         * ARRANGE:
         * El service simulado devuelve el usuario de prueba al guardar.
         */
        when(usuarioService.registrarUsuario(any(Usuario.class)))
                .thenReturn(usuarioEjemplo);

        /*
         * ACT + ASSERT:
         * Enviamos el JSON del usuario y esperamos 201 CREATED.
         */
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEjemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$.correo").value("juan@correo.cl"));

        /*
         * VERIFY:
         */
        verify(usuarioService, times(1)).registrarUsuario(any(Usuario.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 201 but was: 400",
         * algún campo del JSON no pasa las validaciones @NotBlank o @Email.
         * Desarrollo debe verificar que todos los campos requeridos estén presentes.
         */
    }

    // =====================================================================
    // TEST 5: POST /api/usuarios — datos inválidos (400)
    // =====================================================================

    @Test
    @DisplayName("POST /api/usuarios - debe retornar 400 cuando faltan campos obligatorios")
    void guardar_cuandoDatosInvalidos_debeRetornar400() throws Exception {

        /*
         * ARRANGE:
         * Creamos un usuario SIN rut, nombre, correo ni password.
         * Todos son @NotBlank, así que la validación debería fallar.
         *
         * POR QUÉ ESTE TEST ES IMPORTANTE:
         * Sin este test no sabrías si las validaciones @NotBlank y @Email
         * realmente funcionan en producción.
         */
        Usuario usuarioInvalido = new Usuario();
        // No seteamos ningún campo obligatorio

        /*
         * ACT + ASSERT:
         * Spring Validation rechaza el request ANTES de llamar al service.
         * El GlobalExceptionHandler responde 400 Bad Request.
         */
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest());

        /*
         * VERIFY:
         * El service NO debe llamarse porque la validación falló antes.
         */
        verify(usuarioService, never()).registrarUsuario(any());

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 400 but was: 201",
         * las anotaciones @NotBlank no están funcionando.
         * Desarrollo debe verificar que el controller use @Valid
         * y que el pom.xml tenga spring-boot-starter-validation.
         */
    }

    // =====================================================================
    // TEST 6: POST /api/usuarios — correo con formato inválido (400)
    // =====================================================================

    @Test
    @DisplayName("POST /api/usuarios - debe retornar 400 cuando el correo es inválido")
    void guardar_cuandoCorreoInvalido_debeRetornar400() throws Exception {

        /*
         * ARRANGE:
         * Creamos un usuario con todos los campos pero un correo sin formato válido.
         * El campo correo tiene @Email que rechaza textos sin "@".
         */
        Usuario usuarioCorreoMalo = new Usuario();
        usuarioCorreoMalo.setRut("12345678-9");
        usuarioCorreoMalo.setNombre("Juan Perez");
        usuarioCorreoMalo.setCorreo("esto-no-es-un-correo"); // <- inválido
        usuarioCorreoMalo.setPassword("password123");

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioCorreoMalo)))
                .andExpect(status().isBadRequest());

        /*
         * VERIFY:
         */
        verify(usuarioService, never()).registrarUsuario(any());

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 400 but was: 201",
         * la anotación @Email no está validando correctamente.
         * Desarrollo debe revisar el campo correo en la clase Usuario.
         */
    }

    // =====================================================================
    // TEST 7: PUT /api/usuarios/{id} — actualizar exitosamente
    // =====================================================================

    @Test
    @DisplayName("PUT /api/usuarios/1 - debe retornar 200 OK con usuario actualizado")
    void actualizar_cuandoExiste_debeRetornar200() throws Exception {

        /*
         * ARRANGE:
         */
        when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class)))
                .thenReturn(usuarioEjemplo);

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioEjemplo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));

        /*
         * VERIFY:
         */
        verify(usuarioService, times(1)).actualizarUsuario(eq(1L), any(Usuario.class));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 200 but was: 404",
         * el usuario no existe en la BD.
         * Desarrollo debe verificar el flujo de actualización en UsuarioService.
         */
    }

    // =====================================================================
    // TEST 8: DELETE /api/usuarios/{id} — eliminar exitosamente
    // =====================================================================

    @Test
    @DisplayName("DELETE /api/usuarios/1 - debe retornar 204 No Content")
    void eliminar_cuandoExiste_debeRetornar204() throws Exception {

        /*
         * ARRANGE:
         * buscarPorId se llama en el controller antes de eliminar
         * (para verificar que existe). Lo configuramos para que no lance excepción.
         * eliminar() es void, doNothing() indica que no hace nada (simulado).
         */
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioEjemplo);
        doNothing().when(usuarioService).eliminar(1L);

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));;
                

        /*
         * VERIFY:
         * El controller debe llamar buscarPorId ANTES de eliminar.
         */
        verify(usuarioService, times(1)).buscarPorId(1L);
        verify(usuarioService, times(1)).eliminar(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: 204 but was: 404",
         * el usuario no existe o buscarPorId lanza excepción.
         * Desarrollo debe verificar que el usuario existe antes de eliminar.
         */
    }

    // =====================================================================
    // TEST 9: GET /api/usuarios/existe/{id} — verificar existencia (Feign)
    // =====================================================================

    @Test
    @DisplayName("GET /api/usuarios/existe/1 - debe retornar true cuando el usuario existe")
    void verificarExistencia_cuandoExiste_debeRetornarTrue() throws Exception {

        /*
         * ARRANGE:
         * Este endpoint lo usan internamente ms-mascotas y ms-solicitud via Feign
         * para verificar si un usuario existe antes de crear mascotas o solicitudes.
         */
        when(usuarioService.existePorId(1L)).thenReturn(true);

        /*
         * ACT + ASSERT:
         */
        mockMvc.perform(get("/api/usuarios/existe/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        /*
         * VERIFY:
         */
        verify(usuarioService, times(1)).existePorId(1L);

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected: true but was: false",
         * el usuario no existe en la BD o el ID está mal.
         * Si ms-mascotas rechaza crear una mascota porque este endpoint
         * devuelve false, Desarrollo debe verificar que el usuario
         * fue creado correctamente en ms-usuarios.
         */
    }
}