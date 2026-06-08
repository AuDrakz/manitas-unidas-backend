package manitasUnidas.solicitud.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import manitasUnidas.solicitud.model.Solicitud;
import manitasUnidas.solicitud.service.SolicitudService;
import manitasUnidas.solicitud.dto.SolicitudRequestDTO;

import java.util.List;

@Tag(name = "Solicitudes", description = "Gestión de solicitudes de adopción de mascotas")
@Slf4j
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService service;

    @Operation(summary = "Listar todas las solicitudes", description = "Retorna la lista completa de solicitudes de adopción")
    @GetMapping
    public List<Solicitud> listar() {
        log.info("[SolicitudController] GET /api/solicitudes");
        return service.obtenerTodas();
    }

    @Operation(summary = "Buscar solicitud por ID", description = "Retorna los datos de una solicitud específica")
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        log.info("[SolicitudController] GET /api/solicitudes/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear nueva solicitud",
               description = "Crea una solicitud validando: que el adoptante exista, no esté en lista negra, " +
                             "la mascota esté disponible y no tenga otra solicitud pendiente")
    @PostMapping
    public ResponseEntity<Solicitud> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        log.info("[SolicitudController] POST /api/solicitudes - adoptante={}", dto.getIdAdoptante());
        Solicitud nueva = service.crearSolicitud(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar estado de solicitud",
               description = "Cambia el estado de una solicitud (PENDIENTE, APROBADA, RECHAZADA)")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Solicitud> actualizarEstado(@PathVariable Long id,
                                                      @RequestBody String nuevoEstado) {
        String estadoLimpio = nuevoEstado.replace("\"", "").trim();
        log.info("[SolicitudController] PUT /api/solicitudes/{}/estado - nuevoEstado={}", id, estadoLimpio);
        Solicitud actualizada = service.cambiarEstado(id, estadoLimpio);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar solicitud", description = "Elimina una solicitud del sistema por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[SolicitudController] DELETE /api/solicitudes/{}", id);
        service.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}