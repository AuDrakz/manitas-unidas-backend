package manitasUnidas.seguimiento.Controller;


import manitasUnidas.seguimiento.Model.Seguimiento;
import manitasUnidas.seguimiento.Service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguimiento")
public class SeguimientoController {

    @Autowired
    private SeguimientoService service;

    @PostMapping("/actualizar")
    public Seguimiento actualizarEstado(@RequestBody Seguimiento seguimiento) {
        return service.crearSeguimiento(seguimiento);
    }

    @GetMapping("/solicitud/{id}")
    public List<Seguimiento> getHistorial(@PathVariable Long id) {
        return service.obtenerHistorialPorSolicitud(id);
    }



}
