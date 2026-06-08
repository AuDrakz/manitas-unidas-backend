package manitasUnidas.refugios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.refugios.dto.RefugioRequestDTO;
import manitasUnidas.refugios.dto.RefugioResponseDTO;
import manitasUnidas.refugios.service.RefugioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refugios")
@Slf4j
@Tag(name = "Refugios", description = "Gestión de refugios")
public class RefugioController {

    @Autowired
    private RefugioService refugioServ;

    @PostMapping
    @Operation(summary = "Crear un nuevo refugio")
    public RefugioResponseDTO crear(
            @Valid @RequestBody RefugioRequestDTO dto) {

        log.info("Solicitud para crear refugio");

        return refugioServ.guardarRefugio(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todos los refugios")
    public List<RefugioResponseDTO> listar() {

        log.info("Solicitud para listar refugios");

        return refugioServ.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar refugio por ID")
    public RefugioResponseDTO buscarPorId(@PathVariable Long id) {

        log.info("Solicitud para buscar refugio ID: {}", id);

        return refugioServ.buscarPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar refugio por nombre")
    public RefugioResponseDTO buscarPorNombre(
            @PathVariable String nombre) {

        log.info("Solicitud para buscar refugio nombre: {}", nombre);

        return refugioServ.buscarPorNombre(nombre);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar refugios con disponibilidad")
    public List<RefugioResponseDTO> buscarDisponibles() {

        log.info("Solicitud para listar refugios disponibles");

        return refugioServ.buscarConDisponibilidad();
    }

    @GetMapping("/cupos/{id}")
    @Operation(summary = "Consultar cupos disponibles")
    public String verCupos(@PathVariable Long id) {

        log.info("Solicitud para consultar cupos refugio ID: {}", id);

        Integer cupos = refugioServ.obtenerCuposDisponibles(id);

        return "El refugio cuenta con " + cupos + " cupos disponibles.";
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar refugio")
    public RefugioResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RefugioRequestDTO dto) {

        log.info("Solicitud para actualizar refugio ID: {}", id);

        return refugioServ.actualizarRefugio(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar refugio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        log.info("Solicitud para eliminar refugio ID: {}", id);

        refugioServ.eliminarRefugio(id);

        return ResponseEntity.noContent().build();
    }
}