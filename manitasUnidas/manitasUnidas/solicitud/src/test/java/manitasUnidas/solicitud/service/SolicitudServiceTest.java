package manitasUnidas.solicitud.service;

import manitasUnidas.solicitud.client.BlackListClient;
import manitasUnidas.solicitud.client.MascotaClient;
import manitasUnidas.solicitud.client.UsuarioClient;
import manitasUnidas.solicitud.dto.SolicitudRequestDTO;
import manitasUnidas.solicitud.exception.ResourceNotFoundException;
import manitasUnidas.solicitud.exception.SolicitudRechazadaException;
import manitasUnidas.solicitud.model.Solicitud;
import manitasUnidas.solicitud.repository.SolicitudRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - SolicitudService")
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository repository;

    @Mock
    private BlackListClient blackListClient;

    @Mock
    private MascotaClient mascotaClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private SolicitudService solicitudService;

    private SolicitudRequestDTO dtoEjemplo;
    private Solicitud solicitudEjemplo;

    @BeforeEach
    void setUp() {
        dtoEjemplo = new SolicitudRequestDTO(1L, "12345678-9", 1L, "Quiero adoptar");

        solicitudEjemplo = new Solicitud();
        solicitudEjemplo.setId(1L);
        solicitudEjemplo.setIdAdoptante(1L);
        solicitudEjemplo.setRutAdoptante("12345678-9");
        solicitudEjemplo.setIdMascota(1L);
        solicitudEjemplo.setEstado("PENDIENTE");
        solicitudEjemplo.setFechaSolicitud(LocalDate.now());
        solicitudEjemplo.setObservaciones("Quiero adoptar");
    }

    // =====================================================================
    // TESTS: crearSolicitud()
    // =====================================================================

    @Test
    @DisplayName("crearSolicitud - exitoso cuando todas las validaciones pasan")
    void crearSolicitud_exitoso() {
        when(usuarioClient.existeUsuario(1L)).thenReturn(true);
        when(blackListClient.estaBloqueado("12345678-9")).thenReturn(false);
        when(mascotaClient.obtenerEstado(1L)).thenReturn("Disponible");
        when(repository.existsByIdAdoptanteAndEstado(1L, "PENDIENTE")).thenReturn(false);
        when(repository.save(any(Solicitud.class))).thenReturn(solicitudEjemplo);

        Solicitud resultado = solicitudService.crearSolicitud(dtoEjemplo);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(1L, resultado.getIdAdoptante());
        verify(repository, times(1)).save(any(Solicitud.class));
    }

    @Test
    @DisplayName("crearSolicitud - lanza excepción cuando adoptante no existe en ms-usuarios")
    void crearSolicitud_adoptanteNoExiste_lanzaExcepcion() {
        when(usuarioClient.existeUsuario(1L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> solicitudService.crearSolicitud(dtoEjemplo));

        assertTrue(ex.getMessage().contains("no existe en el sistema"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearSolicitud - lanza excepción cuando adoptante está en lista negra")
    void crearSolicitud_adoptanteBloqueado_lanzaExcepcion() {
        when(usuarioClient.existeUsuario(1L)).thenReturn(true);
        when(blackListClient.estaBloqueado("12345678-9")).thenReturn(true);

        SolicitudRechazadaException ex = assertThrows(SolicitudRechazadaException.class,
                () -> solicitudService.crearSolicitud(dtoEjemplo));

        assertTrue(ex.getMessage().contains("lista negra"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearSolicitud - lanza excepción cuando mascota no está disponible")
    void crearSolicitud_mascotaNoDisponible_lanzaExcepcion() {
        when(usuarioClient.existeUsuario(1L)).thenReturn(true);
        when(blackListClient.estaBloqueado("12345678-9")).thenReturn(false);
        when(mascotaClient.obtenerEstado(1L)).thenReturn("Adoptado");

        SolicitudRechazadaException ex = assertThrows(SolicitudRechazadaException.class,
                () -> solicitudService.crearSolicitud(dtoEjemplo));

        assertTrue(ex.getMessage().contains("no está disponible"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearSolicitud - lanza excepción cuando mascota no existe (Feign error)")
    void crearSolicitud_mascotaNoEncontrada_lanzaExcepcion() {
        when(usuarioClient.existeUsuario(1L)).thenReturn(true);
        when(blackListClient.estaBloqueado("12345678-9")).thenReturn(false);
        when(mascotaClient.obtenerEstado(1L)).thenThrow(new RuntimeException("Feign error"));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> solicitudService.crearSolicitud(dtoEjemplo));

        assertTrue(ex.getMessage().contains("no fue encontrada"));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearSolicitud - lanza excepción cuando adoptante ya tiene una solicitud PENDIENTE")
    void crearSolicitud_adoptanteTienePendiente_lanzaExcepcion() {
        when(usuarioClient.existeUsuario(1L)).thenReturn(true);
        when(blackListClient.estaBloqueado("12345678-9")).thenReturn(false);
        when(mascotaClient.obtenerEstado(1L)).thenReturn("Disponible");
        when(repository.existsByIdAdoptanteAndEstado(1L, "PENDIENTE")).thenReturn(true);

        SolicitudRechazadaException ex = assertThrows(SolicitudRechazadaException.class,
                () -> solicitudService.crearSolicitud(dtoEjemplo));

        assertTrue(ex.getMessage().contains("PENDIENTE"));
        verify(repository, never()).save(any());
    }

    // =====================================================================
    // TESTS: obtenerTodas()
    // =====================================================================

    @Test
    @DisplayName("obtenerTodas - retorna lista con solicitudes")
    void obtenerTodas_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(solicitudEjemplo));

        List<Solicitud> resultado = solicitudService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    // =====================================================================
    // TESTS: buscarPorId()
    // =====================================================================

    @Test
    @DisplayName("buscarPorId - retorna solicitud cuando existe")
    void buscarPorId_retornaSolicitud() {
        when(repository.findById(1L)).thenReturn(Optional.of(solicitudEjemplo));

        Solicitud resultado = solicitudService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    @DisplayName("buscarPorId - lanza excepción cuando ID no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> solicitudService.buscarPorId(99L));
    }

    // =====================================================================
    // TESTS: cambiarEstado()
    // =====================================================================

    @Test
    @DisplayName("cambiarEstado - cambia a APROBADA correctamente")
    void cambiarEstado_exitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(solicitudEjemplo));
        solicitudEjemplo.setEstado("APROBADA");
        when(repository.save(any(Solicitud.class))).thenReturn(solicitudEjemplo);

        Solicitud resultado = solicitudService.cambiarEstado(1L, "APROBADA");

        assertEquals("APROBADA", resultado.getEstado());
        verify(repository, times(1)).save(any(Solicitud.class));
    }

    @Test
    @DisplayName("cambiarEstado - lanza excepción cuando solicitud no existe")
    void cambiarEstado_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> solicitudService.cambiarEstado(99L, "APROBADA"));
    }

    // =====================================================================
    // TESTS: eliminarSolicitud()
    // =====================================================================

    @Test
    @DisplayName("eliminarSolicitud - elimina correctamente cuando existe")
    void eliminarSolicitud_exitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(solicitudEjemplo));

        assertDoesNotThrow(() -> solicitudService.eliminarSolicitud(1L));

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarSolicitud - lanza excepción cuando ID no existe")
    void eliminarSolicitud_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> solicitudService.eliminarSolicitud(99L));

        verify(repository, never()).deleteById(anyLong());
    }
}