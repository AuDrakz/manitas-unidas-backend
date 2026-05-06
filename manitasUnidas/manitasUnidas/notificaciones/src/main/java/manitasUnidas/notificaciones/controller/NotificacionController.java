package manitasUnidas.notificaciones.controller;

import manitasUnidas.notificaciones.model.Notificacion;
import manitasUnidas.notificaciones.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    // Endpoint para procesar y enviar una notificación
    @PostMapping("/enviar")
    public ResponseEntity<Notificacion> enviar(@RequestBody Notificacion notificacion) {
        Notificacion nuevaNoti = service.procesarYEnviar(notificacion);
        return ResponseEntity.ok(nuevaNoti);
    }

    // Endpoint opcional para ver el historial de notificaciones enviadas
    @GetMapping("/historial")
    public List<Notificacion> listarTodas() {
        // Aquí usamos el método findAll() que viene gratis en el Repository
        return service.obtenerTodas(); 
    }
}