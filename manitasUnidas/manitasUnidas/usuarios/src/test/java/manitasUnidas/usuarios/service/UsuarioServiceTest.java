package manitasUnidas.usuarios.service;

import manitasUnidas.usuarios.exception.ResourceNotFoundException;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.repository.UsuarioRepository;

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

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios - UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
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
    // TESTS: obtenerTodos()
    // =====================================================================

    @Test
    @DisplayName("obtenerTodos - retorna lista con usuarios existentes")
    void obtenerTodos_retornaLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - retorna lista vacía cuando no hay usuarios")
    void obtenerTodos_listaVacia() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // =====================================================================
    // TESTS: registrarUsuario()
    // =====================================================================

    @Test
    @DisplayName("registrarUsuario - exitoso cuando RUT no está en uso")
    void registrarUsuario_exitoso() {
        when(usuarioRepository.existsByRut("12345678-9")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        Usuario resultado = usuarioService.registrarUsuario(usuarioEjemplo);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("12345678-9", resultado.getRut());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("registrarUsuario - lanza excepción cuando RUT ya está en uso")
    void registrarUsuario_rutDuplicado_lanzaExcepcion() {
        when(usuarioRepository.existsByRut("12345678-9")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.registrarUsuario(usuarioEjemplo));

        assertTrue(ex.getMessage().contains("ya está en uso"));
        verify(usuarioRepository, never()).save(any());
    }

    // =====================================================================
    // TESTS: buscarPorId()
    // =====================================================================

    @Test
    @DisplayName("buscarPorId - retorna usuario cuando existe")
    void buscarPorId_retornaUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Perez", resultado.getNombre());
    }

    @Test
    @DisplayName("buscarPorId - lanza excepción cuando ID no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    // =====================================================================
    // TESTS: actualizarUsuario()
    // =====================================================================

    @Test
    @DisplayName("actualizarUsuario - actualiza datos correctamente")
    void actualizarUsuario_exitoso() {
        Usuario datosNuevos = new Usuario();
        datosNuevos.setNombre("Juan Actualizado");
        datosNuevos.setCorreo("nuevo@correo.cl");
        datosNuevos.setDireccion("Nueva Calle 456");
        datosNuevos.setTelefono("987654321");
        datosNuevos.setRol("Staff");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        usuarioEjemplo.setNombre("Juan Actualizado");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        Usuario resultado = usuarioService.actualizarUsuario(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Juan Actualizado", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizarUsuario - lanza excepción cuando usuario no existe")
    void actualizarUsuario_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> usuarioService.actualizarUsuario(99L, usuarioEjemplo));

        verify(usuarioRepository, never()).save(any());
    }

    // =====================================================================
    // TESTS: eliminar()
    // =====================================================================

    @Test
    @DisplayName("eliminar - elimina correctamente cuando usuario existe")
    void eliminar_exitoso() {
        assertDoesNotThrow(() -> usuarioService.eliminar(1L));

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    // =====================================================================
    // TESTS: existePorId()
    // =====================================================================

    @Test
    @DisplayName("existePorId - retorna true cuando usuario existe")
    void existePorId_retornaTrue() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        assertTrue(usuarioService.existePorId(1L));
    }

    @Test
    @DisplayName("existePorId - retorna false cuando usuario no existe")
    void existePorId_retornaFalse() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertFalse(usuarioService.existePorId(99L));
    }
}