package manitasUnidas.seguimiento.Controller;

import jakarta.validation.Valid;
import manitasUnidas.seguimiento.Model.Seguimiento;
import manitasUnidas.seguimiento.Service.SeguimientoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
public class SeguimientoController {

    @Autowired
    private SeguimientoService service;


    // CREAR SEGUIMIENTO
    @PostMapping
    public Seguimiento crearSeguimiento(@Valid @RequestBody Seguimiento seguimiento) {
        return service.crearSeguimiento(seguimiento);
    }


    // LISTAR TODOS LOS SEGUIMIENTOS
    @GetMapping
    public List<Seguimiento> listarSeguimientos() {
        return service.listarSeguimientos();
    }


    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Seguimiento obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }


    // OBTENER HISTORIAL CLINICO COMPLETO
    @GetMapping("/historial/{fichaVetId}")
    public List<Seguimiento> obtenerHistorialClinico(@PathVariable Long fichaVetId) {
        return service.obtenerHistorialClinico(fichaVetId);
    }


    // BUSCAR POR ESTADO
    @GetMapping("/estado/{estado}")
    public List<Seguimiento> buscarPorEstado(@PathVariable String estado) {
        return service.buscarPorEstado(estado);
    }


    // ACTUALIZAR SEGUIMIENTO
    @PutMapping("/{id}")
    public Seguimiento actualizarSeguimiento(
            @PathVariable Long id,
            @Valid @RequestBody Seguimiento seguimiento) {

        return service.actualizarSeguimiento(id, seguimiento);
    }


    // ELIMINAR SEGUIMIENTO
    @DeleteMapping("/{id}")
    public void eliminarSeguimiento(@PathVariable Long id) {
        service.eliminarSeguimiento(id);
    }
}