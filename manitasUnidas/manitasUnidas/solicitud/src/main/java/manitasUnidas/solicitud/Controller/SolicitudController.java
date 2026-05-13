package manitasUnidas.solicitud.Controller;

import jakarta.validation.Valid; // Necesario para validar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Service.SolicitudService;
import manitasUnidas.solicitud.DTO.SolicitudRequestDTO; // Importa DTO
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
    public ResponseEntity<Solicitud> crear(@Valid @RequestBody SolicitudRequestDTO dto) {
        // Al quitar el try-catch, el código es más legible.
        // Si el service lanza SolicitudRechazadaException, el GlobalExceptionHandler la atrapa.
        Solicitud nueva = service.crearSolicitud(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }
}