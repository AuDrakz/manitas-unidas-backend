package manitasUnidas.notificaciones.controller;

import manitasUnidas.notificaciones.model.Notificacion;
import manitasUnidas.notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "Notificaciones", description = "Envio y consulta de notificaciones a usuarios del sistema")
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    private EntityModel<Notificacion> toModel(Notificacion n) {
        return EntityModel.of(n,
            linkTo(methodOn(NotificacionController.class).listarTodas()).withRel("historial"),
            linkTo(methodOn(NotificacionController.class).enviar(null)).withRel("enviar")
        );
    }

    @Operation(summary = "Enviar notificacion", description = "Procesa y envia una notificacion al correo del usuario")
    @PostMapping("/enviar")
    public ResponseEntity<EntityModel<Notificacion>> enviar(@RequestBody Notificacion notificacion) {
        log.info("[NotificacionController] POST /notificaciones/enviar - correo={}", notificacion.getCorreoUsuario());
        Notificacion nuevaNoti = service.procesarYEnviar(notificacion);
        return ResponseEntity.ok(toModel(nuevaNoti));
    }

    @Operation(summary = "Historial de notificaciones", description = "Retorna todas las notificaciones enviadas")
    @GetMapping("/historial")
    public CollectionModel<EntityModel<Notificacion>> listarTodas() {
        log.info("[NotificacionController] GET /notificaciones/historial");
        List<EntityModel<Notificacion>> lista = service.obtenerTodas().stream().map(this::toModel).toList();
        return CollectionModel.of(lista,
            linkTo(methodOn(NotificacionController.class).listarTodas()).withSelfRel());
    }
}