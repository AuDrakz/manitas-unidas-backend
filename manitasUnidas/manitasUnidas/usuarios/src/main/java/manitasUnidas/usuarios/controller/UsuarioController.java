package manitasUnidas.usuarios.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.service.UsuarioService;

// Importaciones necesarias para OpenAPI/Swagger según la guía Duoc
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Módulo de Gestión de Usuarios", description = "Endpoints para listar, buscar, registrar, modificar y eliminar cuentas de usuario.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "📋 Listar todos los usuarios", 
        description = "Retorna una lista completa con todos los usuarios registrados en el sistema con sus respectivos detalles."
    )
    @ApiResponse(responseCode = "200", description = "¡Éxito! Lista de usuarios obtenida de forma correcta.")
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.obtenerTodos();
    }

    @Operation(
        summary = "🔍 Buscar usuario por su ID", 
        description = "Ingresa el identificador numérico único (ID) para traer la ficha informativa de un usuario específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "¡Usuario localizado con éxito!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No encontrado: El ID consultado no corresponde a ningún usuario en los registros.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(
        summary = "💾 Registrar un nuevo usuario", 
        description = "Envía los datos de un usuario para darlo de alta en el sistema. El RUT, el nombre y el correo electrónico son obligatorios."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "¡Usuario registrado y guardado exitosamente!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "Error de validación: El formato del correo es inválido, faltan campos por llenar o el RUT ya existe.")
    })
    @PostMapping
    public ResponseEntity<Usuario> guardar(@Valid @RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.registrarUsuario(usuario), HttpStatus.CREATED);
    }

    @Operation(
        summary = "🔄 Actualizar datos de un usuario", 
        description = "Permite modificar datos como el nombre, correo, dirección o teléfono de un usuario existente buscando mediante su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "¡Datos del usuario actualizados exitosamente!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo actualizar: El usuario con el ID especificado no existe.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(
        summary = "❌ Eliminar un usuario", 
        description = "Borra permanentemente la cuenta de un usuario del sistema mediante su identificador numérico (ID)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "¡Usuario eliminado de los registros con éxito! No retorna contenido."),
        @ApiResponse(responseCode = "404", description = "No se pudo eliminar: El usuario con el ID proporcionado no fue encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.buscarPorId(id); 
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "⚙️ Verificar si un usuario existe (Uso Interno/Feign)", 
        description = "Devuelve de manera simple 'true' (si existe) o 'false' (si no existe). Diseñado principalmente para la comunicación interna entre microservicios."
    )
    @ApiResponse(responseCode = "200", description = "Consulta completada. Retorna un valor booleano (true/false).")
    @GetMapping("/existe/{id}")
    public ResponseEntity<Boolean> verificarExistencia(@PathVariable Long id) {
        boolean existe = usuarioService.existePorId(id);
        return ResponseEntity.ok(existe);
    }
}