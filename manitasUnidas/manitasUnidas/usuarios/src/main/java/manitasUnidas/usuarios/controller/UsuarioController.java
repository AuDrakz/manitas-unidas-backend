package manitasUnidas.usuarios.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.usuarios.model.Usuario;
import manitasUnidas.usuarios.service.UsuarioService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@Tag(name = "Usuarios", description = "Gestion de usuarios del sistema de adopcion")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).buscarPorId(usuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).listar()).withRel("todos-los-usuarios"),
            linkTo(methodOn(UsuarioController.class).eliminar(usuario.getId())).withRel("eliminar"),
            linkTo(methodOn(UsuarioController.class).verificarExistencia(usuario.getId())).withRel("verificar-existencia")
        );
    }

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listar() {
        log.info("[UsuarioController] GET /api/usuarios");
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerTodos()
                .stream().map(this::toModel).toList();
        return CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public EntityModel<Usuario> buscarPorId(@PathVariable Long id) {
        log.info("[UsuarioController] GET /api/usuarios/{}", id);
        return toModel(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> guardar(@Valid @RequestBody Usuario usuario) {
        log.info("[UsuarioController] POST /api/usuarios");
        EntityModel<Usuario> model = toModel(usuarioService.registrarUsuario(usuario));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public EntityModel<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        log.info("[UsuarioController] PUT /api/usuarios/{}", id);
        return toModel(usuarioService.actualizarUsuario(id, usuario));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[UsuarioController] DELETE /api/usuarios/{}", id);
        usuarioService.buscarPorId(id);
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar existencia (interno Feign)")
    @GetMapping("/existe/{id}")
    public ResponseEntity<Boolean> verificarExistencia(@PathVariable Long id) {
        log.info("[UsuarioController] Verificando existencia usuario ID={}", id);
        return ResponseEntity.ok(usuarioService.existePorId(id));
    }
}