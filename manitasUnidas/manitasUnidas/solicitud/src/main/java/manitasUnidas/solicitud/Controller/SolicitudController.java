package manitasUnidas.solicitud.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Service.SolicitudService;
import manitasUnidas.solicitud.DTO.SolicitudRequestDTO;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService service;

    // 1. Listar todas las solicitudes
    @GetMapping
    public List<Solicitud> listar() {
        return service.obtenerTodas();
    }

    // 2. Obtener una solicitud por ID (Sin .map)
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtenerPorId(@PathVariable Long id) {
        Solicitud solicitud = service.buscarPorId(id);
        if (solicitud != null) {
            return new ResponseEntity<>(solicitud, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 3. Crear una nueva solicitud
    @PostMapping
    public ResponseEntity<Solicitud> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        Solicitud nueva = service.crearSolicitud(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // 4. Actualizar el estado (Aceptada, Rechazada, etc.)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Solicitud> actualizarEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        // Limpiamos las comillas por si vienen en el texto del body
        String estadoLimpio = nuevoEstado.replace("\"", "");
        
        Solicitud actualizada = service.cambiarEstado(id, estadoLimpio);
        
        if (actualizada != null) {
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. Eliminar una solicitud
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = service.eliminarSolicitud(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}