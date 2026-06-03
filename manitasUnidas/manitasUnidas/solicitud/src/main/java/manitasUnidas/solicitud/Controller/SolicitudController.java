package manitasUnidas.solicitud.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Service.SolicitudService;
import manitasUnidas.solicitud.DTO.SolicitudRequestDTO;

import java.util.List;

@Slf4j   // <-- AGREGA ESTE
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService service;

    // GET /api/solicitudes
    @GetMapping
    public List<Solicitud> listar() {
        log.info("[SolicitudController] GET /api/solicitudes");
        return service.obtenerTodas();
    }

    // GET /api/solicitudes/{id}
    // CAMBIO: eliminado el if/null -- ahora el GlobalExceptionHandler maneja el 404
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        log.info("[SolicitudController] GET /api/solicitudes/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // POST /api/solicitudes
    @PostMapping
    public ResponseEntity<Solicitud> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        log.info("[SolicitudController] POST /api/solicitudes - adoptante={}", dto.getIdAdoptante());
        Solicitud nueva = service.crearSolicitud(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/solicitudes/{id}/estado
    // CAMBIO: eliminado el if/null -- ahora el GlobalExceptionHandler maneja el 404
    @PutMapping("/{id}/estado")
    public ResponseEntity<Solicitud> actualizarEstado(@PathVariable Long id,
                                                      @RequestBody String nuevoEstado) {
        String estadoLimpio = nuevoEstado.replace("\"", "").trim();
        log.info("[SolicitudController] PUT /api/solicitudes/{}/estado - nuevoEstado={}", id, estadoLimpio);
        Solicitud actualizada = service.cambiarEstado(id, estadoLimpio);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/solicitudes/{id}
    // CAMBIO: eliminado el if/boolean -- ahora el GlobalExceptionHandler maneja el 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[SolicitudController] DELETE /api/solicitudes/{}", id);
        service.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}