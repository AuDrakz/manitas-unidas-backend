package manitasUnidas.blackList.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import manitasUnidas.blackList.model.BlackList;
import manitasUnidas.blackList.service.BlackListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/blacklist")
@Tag(name = "Modulo de Lista Negra (Blacklist)", description = "Endpoints para controlar la restriccion y el bloqueo de acceso de usuarios.")
public class BlackListController {

    @Autowired
    private BlackListService service;

    private EntityModel<BlackList> toModel(BlackList bl) {
        return EntityModel.of(bl,
            linkTo(methodOn(BlackListController.class).buscarPorId(bl.getId())).withSelfRel(),
            linkTo(methodOn(BlackListController.class).listar()).withRel("todos-los-bloqueos"),
            linkTo(methodOn(BlackListController.class).eliminar(bl.getId())).withRel("desbloquear")
        );
    }

    @Operation(summary = "Listar registros de la Lista Negra")
    @ApiResponse(responseCode = "200", description = "Lista de bloqueos obtenida correctamente.")
    @GetMapping
    public CollectionModel<EntityModel<BlackList>> listar() {
        List<EntityModel<BlackList>> lista = service.obtenerTodos().stream().map(this::toModel).toList();
        return CollectionModel.of(lista, linkTo(methodOn(BlackListController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Buscar un bloqueo por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro localizado.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlackList.class))),
        @ApiResponse(responseCode = "404", description = "ID no encontrado en lista negra.")
    })
    @GetMapping("/{id}")
    public EntityModel<BlackList> buscarPorId(@PathVariable Long id) {
        return toModel(service.obtenerPorId(id));
    }

    @Operation(summary = "Verificar si un RUT esta bloqueado")
    @ApiResponse(responseCode = "200", description = "Retorna true/false.")
    @GetMapping("/verificar/{rut}")
    public ResponseEntity<Boolean> estaBloqueado(@PathVariable String rut) {
        try {
            service.buscarPorRut(rut);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @Operation(summary = "Agregar un usuario a la Lista Negra")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario agregado exitosamente.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlackList.class))),
        @ApiResponse(responseCode = "400", description = "Datos invalidos.")
    })
    @PostMapping
    public ResponseEntity<EntityModel<BlackList>> agregar(@Valid @RequestBody BlackList registro) {
        return new ResponseEntity<>(toModel(service.bloquearUsuario(registro)), HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar los datos de un bloqueo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro actualizado.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlackList.class))),
        @ApiResponse(responseCode = "404", description = "ID no encontrado.")
    })
    @PutMapping("/{id}")
    public EntityModel<BlackList> actualizar(@PathVariable Long id, @RequestBody BlackList blackList) {
        return toModel(service.actualizarBlackList(id, blackList));
    }

    @Operation(summary = "Quitar de la Lista Negra (Desbloquear)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario desbloqueado."),
        @ApiResponse(responseCode = "404", description = "ID no encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.desbloquear(id);
        return ResponseEntity.noContent().build();
    }
}