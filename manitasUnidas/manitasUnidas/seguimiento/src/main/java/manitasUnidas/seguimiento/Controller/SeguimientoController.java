package manitasUnidas.seguimiento.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.seguimiento.Service.SeguimientoService;
import manitasUnidas.seguimiento.dto.SeguimientoRequestDTO;
import manitasUnidas.seguimiento.dto.SeguimientoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador encargado de la gestión de seguimientos clínicos.
 */
@RestController
@RequestMapping("/api/seguimientos")
@Tag(
        name = "Seguimientos",
        description = "Gestión de seguimientos clínicos veterinarios"
)
@Slf4j
public class SeguimientoController {

    @Autowired
    private SeguimientoService service;

    /**
     * Registra un nuevo seguimiento.
     */
    @PostMapping
    @Operation(summary = "Crear seguimiento")
    public ResponseEntity<SeguimientoResponseDTO> crearSeguimiento(
            @Valid @RequestBody SeguimientoRequestDTO dto) {

        log.info(
                "Solicitud para crear seguimiento fichaVetId={}",
                dto.getFichaVetId());

        return ResponseEntity.ok(
                service.crearSeguimiento(dto));
    }

    /**
     * Lista todos los seguimientos.
     */
    @GetMapping
    @Operation(summary = "Listar seguimientos")
    public ResponseEntity<List<SeguimientoResponseDTO>>
    listarSeguimientos() {

        log.info("Solicitud para listar seguimientos");

        return ResponseEntity.ok(
                service.listarSeguimientos());
    }

    /**
     * Busca un seguimiento por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar seguimiento por ID")
    public ResponseEntity<SeguimientoResponseDTO>
    obtenerPorId(@PathVariable Long id) {

        log.info(
                "Solicitud para buscar seguimiento ID={}",
                id);

        return ResponseEntity.ok(
                service.obtenerPorId(id));
    }

    /**
     * Obtiene historial clínico por ficha veterinaria.
     */
    @GetMapping("/historial/{fichaVetId}")
    @Operation(summary = "Obtener historial clínico")
    public ResponseEntity<List<SeguimientoResponseDTO>>
    obtenerHistorialClinico(
            @PathVariable Long fichaVetId) {

        log.info(
                "Solicitud historial fichaVetId={}",
                fichaVetId);

        return ResponseEntity.ok(
                service.obtenerHistorialClinico(
                        fichaVetId));
    }

    /**
     * Busca seguimientos por estado.
     */
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar seguimientos por estado")
    public ResponseEntity<List<SeguimientoResponseDTO>>
    buscarPorEstado(
            @PathVariable String estado) {

        log.info(
                "Solicitud búsqueda estado={}",
                estado);

        return ResponseEntity.ok(
                service.buscarPorEstado(estado));
    }

    /**
     * Actualiza un seguimiento.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar seguimiento")
    public ResponseEntity<SeguimientoResponseDTO>
    actualizarSeguimiento(
            @PathVariable Long id,
            @Valid @RequestBody SeguimientoRequestDTO dto) {

        log.info(
                "Solicitud actualización seguimiento ID={}",
                id);

        return ResponseEntity.ok(
                service.actualizarSeguimiento(
                        id,
                        dto));
    }

    /**
     * Elimina un seguimiento.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar seguimiento")
    public ResponseEntity<Void>
    eliminarSeguimiento(
            @PathVariable Long id) {

        log.warn(
                "Solicitud eliminación seguimiento ID={}",
                id);

        service.eliminarSeguimiento(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si existe seguimiento asociado
     * a una ficha veterinaria.
     */
    @GetMapping("/existe/{fichaVetId}")
    @Operation(summary = "Verificar existencia de seguimiento")
    public ResponseEntity<Boolean>
    existeFichaVet(
            @PathVariable Long fichaVetId) {

        log.info(
                "Validando existencia fichaVetId={}",
                fichaVetId);

        return ResponseEntity.ok(
                service.existeFichaVet(fichaVetId));
    }
}