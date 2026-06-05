package manitasUnidas.seguimiento.Controller;

import jakarta.validation.Valid;
import manitasUnidas.seguimiento.Model.Seguimiento;
import manitasUnidas.seguimiento.Service.SeguimientoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
@Tag(name = "Seguimientos", description = "Gestión de seguimientos clínicos veterinarios")
public class SeguimientoController {

    private static final Logger logger =
            LoggerFactory.getLogger(SeguimientoController.class);

    private final SeguimientoService service;

    public SeguimientoController(SeguimientoService service) {
        this.service = service;
    }

    // CREAR SEGUIMIENTO
    @PostMapping
    @Operation(summary = "Crear seguimiento", description = "Registra un nuevo seguimiento clínico")
    public Seguimiento crearSeguimiento(@Valid @RequestBody Seguimiento seguimiento) {

        logger.info("Request: crear seguimiento fichaVetId={}", seguimiento.getFichaVetId());

        Seguimiento creado = service.crearSeguimiento(seguimiento);

        logger.info("Seguimiento creado ID={}", creado.getId());

        return creado;
    }

    // LISTAR TODOS
    @GetMapping
    @Operation(summary = "Listar seguimientos", description = "Obtiene todos los seguimientos registrados")
    public List<Seguimiento> listarSeguimientos() {

        logger.info("Request: listar seguimientos");

        return service.listarSeguimientos();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener seguimiento por ID")
    public Seguimiento obtenerPorId(@PathVariable Long id) {

        logger.info("Request: obtener seguimiento ID={}", id);

        return service.obtenerPorId(id);
    }

    // HISTORIAL CLÍNICO
    @GetMapping("/historial/{fichaVetId}")
    @Operation(summary = "Historial clínico por ficha veterinaria")
    public List<Seguimiento> obtenerHistorialClinico(@PathVariable Long fichaVetId) {

        logger.info("Request: historial fichaVetId={}", fichaVetId);

        return service.obtenerHistorialClinico(fichaVetId);
    }

    // POR ESTADO
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar por estado")
    public List<Seguimiento> buscarPorEstado(@PathVariable String estado) {

        logger.info("Request: buscar por estado={}", estado);

        return service.buscarPorEstado(estado);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar seguimiento")
    public Seguimiento actualizarSeguimiento(
            @PathVariable Long id,
            @Valid @RequestBody Seguimiento seguimiento) {

        logger.info("Request: actualizar seguimiento ID={}", id);

        return service.actualizarSeguimiento(id, seguimiento);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar seguimiento")
    public void eliminarSeguimiento(@PathVariable Long id) {

        logger.warn("Request: eliminar seguimiento ID={}", id);

        service.eliminarSeguimiento(id);
    }
}