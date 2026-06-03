// ============================================================
// ARCHIVO 1: UsuarioService.java
// Ruta: manitasUnidas/usuarios/service/UsuarioService.java
// CAMBIO: agregar @Slf4j y logs en cada método
// ============================================================

package manitasUnidas.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import manitasUnidas.usuarios.exception.ResourceNotFoundException;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.repository.UsuarioRepository;

@Slf4j   // <-- AGREGA ESTE
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        log.info("[UsuarioService] Consultando todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        log.info("[UsuarioService] Intentando registrar usuario con RUT={}", usuario.getRut());
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            log.warn("[UsuarioService] RUT={} ya existe en el sistema", usuario.getRut());
            throw new RuntimeException("No se puede registrar: El RUT " + usuario.getRut() + " ya está en uso.");
        }
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("[UsuarioService] Usuario registrado con ID={}", guardado.getId());
        return guardado;
    }

    public Usuario buscarPorId(Long id) {
        log.info("[UsuarioService] Buscando usuario con ID={}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[UsuarioService] Usuario con ID={} no encontrado", id);
                    return new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
                });
    }

    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        log.info("[UsuarioService] Actualizando usuario con ID={}", id);
        Usuario usuarioExistente = buscarPorId(id);
        usuarioExistente.setNombre(datosNuevos.getNombre());
        usuarioExistente.setCorreo(datosNuevos.getCorreo());
        usuarioExistente.setDireccion(datosNuevos.getDireccion());
        usuarioExistente.setTelefono(datosNuevos.getTelefono());
        usuarioExistente.setRol(datosNuevos.getRol());
        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        log.info("[UsuarioService] Usuario ID={} actualizado exitosamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("[UsuarioService] Eliminando usuario con ID={}", id);
        usuarioRepository.deleteById(id);
        log.info("[UsuarioService] Usuario ID={} eliminado", id);
    }

    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }
}