package manitasUnidas.notificaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import manitasUnidas.notificaciones.model.Notificacion;
import manitasUnidas.notificaciones.service.NotificacionService;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @Operation(
        summary = "Obtener todas las notificaciones",
        description = "Retorna un listado completo de todas las notificaciones registradas en el sistema."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Notificacion.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @Operation(
        summary = "Enviar y registrar una nueva notificación",
        description = "Procesa el envío de una nueva alerta al usuario, genera el mensaje correspondiente según el estado de la solicitud y guarda el registro en la base de datos."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Notificación enviada y registrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Notificacion.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error de validación. Faltan datos obligatorios."
        )
    })
    @PostMapping
    public ResponseEntity<Notificacion> enviar(
            @Valid @RequestBody Notificacion notificacion) {

        Notificacion nueva = service.procesarYEnviar(notificacion);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }
}