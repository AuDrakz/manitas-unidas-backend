package manitasUnidas.usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manitasUnidas.usuarios.exception.ResourceNotFoundException;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // metodo para obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // metodo para registrar usuario
    public Usuario registrarUsuario(Usuario usuario) {
        // verificamos si es que ya existe un usuario con el rut de usuario
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new RuntimeException("No se puede registrar: El RUT " + usuario.getRut());
        }
        // en caso de que no exista se registra el nuevo usuario
        return usuarioRepository.save(usuario);
    }

    // metodo para buscar un usuario mediante el Id
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        // Buscamos por ID usando el método que ya tienes
        Usuario usuarioExistente = buscarPorId(id);
        
        // Actualizamos los campos
        usuarioExistente.setNombre(datosNuevos.getNombre());
        usuarioExistente.setCorreo(datosNuevos.getCorreo());
        usuarioExistente.setDireccion(datosNuevos.getDireccion());
        usuarioExistente.setTelefono(datosNuevos.getTelefono());
        usuarioExistente.setRol(datosNuevos.getRol());
        
        // Guardamos los cambios en el repository
        return usuarioRepository.save(usuarioExistente);
    }

    // metodo para eliminar un usuario mediante su Id
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }
}
