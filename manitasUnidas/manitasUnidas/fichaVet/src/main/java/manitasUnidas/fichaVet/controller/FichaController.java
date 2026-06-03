package manitasUnidas.fichaVet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.fichaVet.dto.FichaRequestDTO;
import manitasUnidas.fichaVet.dto.FichaResponseDTO;
import manitasUnidas.fichaVet.service.FichaService;

@Slf4j
@RestController
@RequestMapping("/api/fichavet")
public class FichaController {

    private final FichaService fichaServ;

    public FichaController(FichaService fichaServ) {
        this.fichaServ = fichaServ;
    }

    // CREAR
    @PostMapping
    public ResponseEntity<FichaResponseDTO> crearFicha(
            @Valid @RequestBody FichaRequestDTO dto) {

        log.info("Creando ficha veterinaria para mascota {}", dto.getIdMascota());

        return ResponseEntity.status(201)
                .body(fichaServ.saveFicha(dto));
    }

    // TRAER TODAS
    @GetMapping
    public ResponseEntity<List<FichaResponseDTO>> traerFichas() {

        log.info("Obteniendo todas las fichas veterinarias");

        return ResponseEntity.ok(fichaServ.getFichas());
    }

    // TRAER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<FichaResponseDTO> traerFicha(@PathVariable Long id) {

        log.info("Buscando ficha {}", id);

        return ResponseEntity.ok(fichaServ.findFicha(id));
    }

    // HISTORIAL POR MASCOTA
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<FichaResponseDTO>> historialPorMascota(
            @PathVariable Long idMascota) {

        log.info("Consultando historial de mascota {}", idMascota);

        return ResponseEntity.ok(fichaServ.findByMascota(idMascota));
    }

    // BUSCAR POR VETERINARIO
    @GetMapping("/veterinario/{rut}")
    public ResponseEntity<List<FichaResponseDTO>> buscarPorRut(
            @PathVariable String rut) {

        log.info("Consultando fichas del veterinario {}", rut);

        return ResponseEntity.ok(fichaServ.findByRutVeterinario(rut));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFicha(@PathVariable Long id) {

        log.warn("Eliminando ficha {}", id);

        fichaServ.deleteFicha(id);

        return ResponseEntity.noContent().build();
    }

    // EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<FichaResponseDTO> editarFicha(
            @PathVariable Long id,
            @Valid @RequestBody FichaRequestDTO dto) {

        log.info("Actualizando ficha {}", id);

        return ResponseEntity.ok(fichaServ.editFicha(id, dto));
    }
}