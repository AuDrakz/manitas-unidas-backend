package manitasUnidas.usuarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.service.UsuarioService;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema de adopción")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todos los usuarios", description = "Retorna la lista completa de usuarios registrados")
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.obtenerTodos();
    }

    @Operation(summary = "Buscar usuario por ID", description = "Retorna los datos de un usuario específico")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Registrar nuevo usuario",
               description = "Crea un nuevo usuario. Valida campos obligatorios como nombre, correo y RUT")
    @PostMapping
    public ResponseEntity<Usuario> guardar(@Valid @RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.registrarUsuario(usuario), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.buscarPorId(id);
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar existencia (interno)",
               description = "Usado por ms-mascotas y ms-solicitud vía Feign para verificar si un usuario existe")
    @GetMapping("/existe/{id}")
    public ResponseEntity<Boolean> verificarExistencia(@PathVariable Long id) {
        boolean existe = usuarioService.existePorId(id);
        return ResponseEntity.ok(existe);
    }
}