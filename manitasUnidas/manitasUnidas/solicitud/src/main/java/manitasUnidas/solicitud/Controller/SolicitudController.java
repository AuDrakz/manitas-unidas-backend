package manitasUnidas.solicitud.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import manitasUnidas.solicitud.model.Solicitud;
import manitasUnidas.solicitud.service.SolicitudService;
import manitasUnidas.solicitud.dto.SolicitudRequestDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

@Tag(name = "Solicitudes", description = "Gestion de solicitudes de adopcion")
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private static final Logger log = LoggerFactory.getLogger(SolicitudController.class);

    @Autowired
    private SolicitudService service;

    private EntityModel<Solicitud> toModel(Solicitud s) {
        return EntityModel.of(s,
            linkTo(methodOn(SolicitudController.class).obtenerPorId(s.getId())).withSelfRel(),
            linkTo(methodOn(SolicitudController.class).listar()).withRel("todas-las-solicitudes"),
            linkTo(methodOn(SolicitudController.class).actualizarEstado(s.getId(), "APROBADA")).withRel("actualizar-estado"),
            linkTo(methodOn(SolicitudController.class).eliminar(s.getId())).withRel("eliminar")
        );
    }

    @Operation(summary = "Listar todas las solicitudes")
    @GetMapping
    public CollectionModel<EntityModel<Solicitud>> listar() {
        log.info("[SolicitudController] GET /api/solicitudes");
        List<EntityModel<Solicitud>> lista = service.obtenerTodas().stream().map(this::toModel).toList();
        return CollectionModel.of(lista, linkTo(methodOn(SolicitudController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Buscar solicitud por ID")
    @GetMapping("/{id}")
    public EntityModel<Solicitud> obtenerPorId(@PathVariable Long id) {
        log.info("[SolicitudController] GET /api/solicitudes/{}", id);
        return toModel(service.buscarPorId(id));
    }

    @Operation(summary = "Crear nueva solicitud")
    @PostMapping
    public ResponseEntity<EntityModel<Solicitud>> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        log.info("[SolicitudController] POST /api/solicitudes - adoptante={}", dto.getIdAdoptante());
        return new ResponseEntity<>(toModel(service.crearSolicitud(dto)), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar estado (PENDIENTE / APROBADA / RECHAZADA)")
    @PutMapping("/{id}/estado")
    public EntityModel<Solicitud> actualizarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        String estadoLimpio = nuevoEstado.replace("\"", "").trim();
        log.info("[SolicitudController] PUT /api/solicitudes/{}/estado - {}", id, estadoLimpio);
        return toModel(service.cambiarEstado(id, estadoLimpio));
    }

    @Operation(summary = "Eliminar solicitud")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[SolicitudController] DELETE /api/solicitudes/{}", id);
        service.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}