package manitasUnidas.denuncias.controller;

import manitasUnidas.denuncias.model.Denuncia;
import manitasUnidas.denuncias.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

@Slf4j
@Tag(name = "Denuncias", description = "Gestion de denuncias por maltrato o abandono animal")
@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService service;

    private EntityModel<Denuncia> toModel(Denuncia d) {
        return EntityModel.of(d,
            linkTo(methodOn(DenunciaController.class).obtenerPorId(d.getId())).withSelfRel(),
            linkTo(methodOn(DenunciaController.class).listar()).withRel("todas-las-denuncias"),
            linkTo(methodOn(DenunciaController.class).eliminar(d.getId())).withRel("eliminar")
        );
    }

    @Operation(summary = "Listar todas las denuncias")
    @GetMapping
    public CollectionModel<EntityModel<Denuncia>> listar() {
        log.info("[DenunciaController] GET /api/denuncias");
        List<EntityModel<Denuncia>> lista = service.listarTodas().stream().map(this::toModel).toList();
        return CollectionModel.of(lista, linkTo(methodOn(DenunciaController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Registrar nueva denuncia")
    @PostMapping
    public ResponseEntity<EntityModel<Denuncia>> crear(@RequestBody Denuncia denuncia) {
        log.info("[DenunciaController] POST /api/denuncias - denuncianteId={}", denuncia.getDenuncianteId());
        return new ResponseEntity<>(toModel(service.guardar(denuncia)), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar denuncia por ID")
    @GetMapping("/{id}")
    public EntityModel<Denuncia> obtenerPorId(@PathVariable Long id) {
        log.info("[DenunciaController] GET /api/denuncias/{}", id);
        return toModel(service.buscarPorId(id));
    }

    @Operation(summary = "Actualizar estado de denuncia (EN_REVISION, RESUELTA, CERRADA)")
    @PutMapping("/{id}")
    public EntityModel<Denuncia> actualizarEstado(@PathVariable Long id, @RequestBody Denuncia denunciaDetalles) {
        log.info("[DenunciaController] PUT /api/denuncias/{} - nuevoEstado={}", id, denunciaDetalles.getEstado());
        return toModel(service.actualizarEstado(id, denunciaDetalles.getEstado()));
    }

    @Operation(summary = "Eliminar denuncia")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[DenunciaController] DELETE /api/denuncias/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}