package manitasUnidas.mascotas.service;

import manitasUnidas.mascotas.client.RefugioClient;
import manitasUnidas.mascotas.client.UsuarioClient;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.dto.MascotaResponseDTO;
import manitasUnidas.mascotas.exception.ResourceNotFoundException;
import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.repository.MascotaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitario de la capa Service para ms-mascotas.
 *
 * QUÉ SE PRUEBA AQUÍ:
 * - La lógica de negocio: validaciones, casos de éxito y de error.
 * - Que el service llame correctamente al repository y los Feign Clients.
 *
 * QUÉ NO SE PRUEBA AQUÍ:
 * - Los endpoints HTTP (eso es MascotaControllerTest).
 * - La base de datos real (usamos @Mock del repository).
 * - Eureka ni otros microservicios (los Feign Clients son mocks).
 *
 * POR QUÉ @ExtendWith(MockitoExtension.class):
 * Habilita Mockito sin levantar Spring. Es el enfoque más liviano y rápido.
 * No necesita MySQL, no necesita Eureka, corre en milisegundos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - MascotaService")
class MascotaServiceTest {

    // @Mock crea versiones simuladas de las dependencias.
    // El service usará estos mocks en vez de las implementaciones reales.
    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private RefugioClient refugioClient;

    // @InjectMocks crea el MascotaService real e inyecta los mocks anteriores.
    @InjectMocks
    private MascotaService mascotaService;

    // Datos de prueba reutilizables en todos los tests.
    private Mascota mascotaEjemplo;
    private MascotaRequestDTO dtoEjemplo;

    @BeforeEach
    void setUp() {
        // Entidad que simula lo que devuelve el repository
        mascotaEjemplo = new Mascota();
        mascotaEjemplo.setId(1L);
        mascotaEjemplo.setNombre("Luna");
        mascotaEjemplo.setEspecie("Perro");
        mascotaEjemplo.setRaza("Labrador");
        mascotaEjemplo.setEdad(3);
        mascotaEjemplo.setSexo("Hembra");
        mascotaEjemplo.setDescripcion("Muy amigable");
        mascotaEjemplo.setEstado("Disponible");
        mascotaEjemplo.setDuenoId(1L);
        mascotaEjemplo.setRefugioId(1L);

        // DTO que simula lo que envía el cliente en el body del POST/PUT
        dtoEjemplo = new MascotaRequestDTO();
        dtoEjemplo.setNombre("Luna");
        dtoEjemplo.setEspecie("Perro");
        dtoEjemplo.setRaza("Labrador");
        dtoEjemplo.setEdad(3);
        dtoEjemplo.setSexo("Hembra");
        dtoEjemplo.setDescripcion("Muy amigable");
        dtoEjemplo.setEstado("Disponible");
        dtoEjemplo.setDuenoId(1L);
        dtoEjemplo.setRefugioId(1L);
    }

    // =====================================================================
    // TESTS: obtenerTodas()
    // =====================================================================

    @Test
    @DisplayName("obtenerTodas - retorna lista con mascotas existentes")
    void obtenerTodas_retornaLista() {
        // ARRANGE: el repository simulado devuelve una lista con una mascota
        when(mascotaRepository.findAll()).thenReturn(List.of(mascotaEjemplo));

        // ACT: llamamos al método real del service
        List<MascotaResponseDTO> resultado = mascotaService.obtenerTodas();

        // ASSERT: verificamos que la respuesta sea correcta
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Luna", resultado.get(0).getNombre());

        // VERIFY: confirmamos que el service llamó al repository
        verify(mascotaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodas - retorna lista vacía cuando no hay mascotas")
    void obtenerTodas_listaVacia() {
        // ARRANGE
        when(mascotaRepository.findAll()).thenReturn(List.of());

        // ACT
        List<MascotaResponseDTO> resultado = mascotaService.obtenerTodas();

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // =====================================================================
    // TESTS: registrarMascota()
    // =====================================================================

    @Test
    @DisplayName("registrarMascota - exitoso cuando usuario y refugio existen")
    void registrarMascota_exitoso() {
        // ARRANGE
        when(usuarioClient.verificarExistencia(1L)).thenReturn(true);
        when(refugioClient.verificarExistencia(1L)).thenReturn(true);
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaEjemplo);

        // ACT
        MascotaResponseDTO resultado = mascotaService.registrarMascota(dtoEjemplo);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Luna", resultado.getNombre());
        assertEquals("Disponible", resultado.getEstado());

        // VERIFY
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("registrarMascota - lanza excepción cuando el usuario (dueño) no existe")
    void registrarMascota_usuarioNoExiste_lanzaExcepcion() {
        // ARRANGE: el Feign Client de usuarios dice que el dueño no existe
        when(usuarioClient.verificarExistencia(1L)).thenReturn(false);

        // ACT + ASSERT: debe lanzar ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class,
                () -> mascotaService.registrarMascota(dtoEjemplo));

        // VERIFY: el repository NO debe llamarse porque falló la validación
        verify(mascotaRepository, never()).save(any());

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si este test falla con "Expected ResourceNotFoundException but nothing was thrown",
         * significa que el service no valida la existencia del dueño antes de guardar.
         * Desarrollo debe agregar la validación de usuarioClient en registrarMascota().
         */
    }

    @Test
    @DisplayName("registrarMascota - lanza excepción cuando el refugio no existe")
    void registrarMascota_refugioNoExiste_lanzaExcepcion() {
        // ARRANGE
        when(usuarioClient.verificarExistencia(1L)).thenReturn(true);
        when(refugioClient.verificarExistencia(1L)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> mascotaService.registrarMascota(dtoEjemplo));

        // VERIFY
        verify(mascotaRepository, never()).save(any());

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla, el service no valida la existencia del refugio.
         * Desarrollo debe agregar la validación de refugioClient en registrarMascota().
         */
    }

    // =====================================================================
    // TESTS: buscarPorId()
    // =====================================================================

    @Test
    @DisplayName("buscarPorId - retorna DTO cuando la mascota existe")
    void buscarPorId_retornaMascota() {
        // ARRANGE
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaEjemplo));

        // ACT
        MascotaResponseDTO resultado = mascotaService.buscarPorId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Luna", resultado.getNombre());

        // VERIFY
        verify(mascotaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId - lanza excepción cuando el ID no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        // ARRANGE: el repository no encuentra nada para el ID 99
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> mascotaService.buscarPorId(99L));

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla, el service devuelve null en vez de lanzar excepción.
         * Desarrollo debe asegurarse de usar orElseThrow() en findById().
         */
    }

    // =====================================================================
    // TESTS: actualizarMascota()
    // =====================================================================

    @Test
    @DisplayName("actualizarMascota - actualiza correctamente cuando todo existe")
    void actualizarMascota_exitoso() {
        // ARRANGE
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaEjemplo));
        when(usuarioClient.verificarExistencia(1L)).thenReturn(true);
        when(refugioClient.verificarExistencia(1L)).thenReturn(true);
        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaEjemplo);

        // ACT
        MascotaResponseDTO resultado = mascotaService.actualizarMascota(1L, dtoEjemplo);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Luna", resultado.getNombre());

        // VERIFY
        verify(mascotaRepository, times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("actualizarMascota - lanza excepción cuando la mascota no existe")
    void actualizarMascota_mascotaNoExiste_lanzaExcepcion() {
        // ARRANGE
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> mascotaService.actualizarMascota(99L, dtoEjemplo));

        // VERIFY
        verify(mascotaRepository, never()).save(any());
    }

    // =====================================================================
    // TESTS: eliminar()
    // =====================================================================

    @Test
    @DisplayName("eliminar - elimina correctamente cuando la mascota existe")
    void eliminar_exitoso() {
        // ARRANGE
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaEjemplo));

        // ACT
        assertDoesNotThrow(() -> mascotaService.eliminar(1L));

        // VERIFY: debe haberse llamado delete o deleteById (según implementación)
        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, atLeastOnce()).delete(any(Mascota.class));
    }

    @Test
    @DisplayName("eliminar - lanza excepción cuando el ID no existe")
    void eliminar_noExiste_lanzaExcepcion() {
        // ARRANGE
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> mascotaService.eliminar(99L));

        // VERIFY: no debe intentar eliminar si no existe
        verify(mascotaRepository, never()).delete(any());

        /*
         * CASO HIPOTÉTICO DE FALLA PARA QA:
         * Si falla con "Expected exception but nothing was thrown",
         * el service está intentando eliminar sin verificar primero si existe.
         * Desarrollo debe agregar buscarPorId() antes de deleteById().
         */
    }

    // =====================================================================
    // TESTS: obtenerEstado()
    // =====================================================================

    @Test
    @DisplayName("obtenerEstado - retorna estado correcto de la mascota")
    void obtenerEstado_retornaEstado() {
        // ARRANGE
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascotaEjemplo));

        // ACT
        String estado = mascotaService.obtenerEstado(1L);

        // ASSERT
        assertEquals("Disponible", estado);
    }

    @Test
    @DisplayName("obtenerEstado - lanza excepción cuando la mascota no existe")
    void obtenerEstado_noExiste_lanzaExcepcion() {
        // ARRANGE
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class,
                () -> mascotaService.obtenerEstado(99L));
    }

    // =====================================================================
    // TESTS: existePorId()
    // =====================================================================

    @Test
    @DisplayName("existePorId - retorna true cuando la mascota existe")
    void existePorId_retornaTrue() {
        when(mascotaRepository.existsById(1L)).thenReturn(true);
        assertTrue(mascotaService.existePorId(1L));
    }

    @Test
    @DisplayName("existePorId - retorna false cuando la mascota no existe")
    void existePorId_retornaFalse() {
        when(mascotaRepository.existsById(99L)).thenReturn(false);
        assertFalse(mascotaService.existePorId(99L));
    }
}