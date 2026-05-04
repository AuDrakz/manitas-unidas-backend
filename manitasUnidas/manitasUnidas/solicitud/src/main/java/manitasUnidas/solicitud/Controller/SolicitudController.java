package manitasUnidas.solicitud.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Service.SolicitudService;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    @Autowired
    private SolicitudService service;

    @GetMapping
    public List<Solicitud> listar() {
        return service.obtenerTodas();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Solicitud solicitud) {
        try {
            Solicitud nueva = service.crearSolicitud(solicitud);
            return new ResponseEntity<>(nueva, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Enviamos el mensaje de error si está en la lista negra
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
