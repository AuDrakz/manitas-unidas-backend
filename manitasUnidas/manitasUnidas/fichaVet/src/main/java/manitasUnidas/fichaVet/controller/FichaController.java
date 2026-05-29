package manitasUnidas.fichaVet.controller;

import manitasUnidas.fichaVet.model.Ficha;
import manitasUnidas.fichaVet.service.FichaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/fichavet")
public class FichaController {

    private final FichaService fichaServ;

    public FichaController(FichaService fichaServ) {
        this.fichaServ = fichaServ;
    }

    // 1. CREAR
    @PostMapping
    public ResponseEntity<Ficha> crearFicha(@Valid @RequestBody Ficha ficha) {

        log.info("Creando ficha veterinaria para mascota {}", ficha.getIdMascota());

        fichaServ.saveFicha(ficha);

        return ResponseEntity.status(201).body(ficha);
    }

    // 2. TRAER TODAS
    @GetMapping
    public ResponseEntity<List<Ficha>> traerFichas() {

        log.info("Obteniendo todas las fichas veterinarias");

        return ResponseEntity.ok(fichaServ.getFichas());
    }

    // 3. TRAER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Ficha> traerFicha(@PathVariable Long id) {

        log.info("Buscando ficha {}", id);

        return ResponseEntity.ok(fichaServ.findFicha(id));
    }

    // 4. HISTORIAL POR MASCOTA
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<Ficha>> historialPorMascota(@PathVariable Long idMascota) {

        log.info("Historial de mascota {}", idMascota);

        return ResponseEntity.ok(fichaServ.findByMascota(idMascota));
    }

    // 5. BUSCAR POR VETERINARIO
    @GetMapping("/veterinario/{rut}")
    public ResponseEntity<List<Ficha>> buscarPorRut(@PathVariable String rut) {

        log.info("Buscando fichas del veterinario {}", rut);

        return ResponseEntity.ok(fichaServ.findByRutVeterinario(rut));
    }

    // 6. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFicha(@PathVariable Long id) {

        log.warn("Eliminando ficha {}", id);

        fichaServ.deleteFicha(id);

        return ResponseEntity.noContent().build();
    }

    // 7. EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<Ficha> editarFicha(
            @PathVariable Long id,
            @Valid @RequestBody Ficha ficha) {

        log.info("Actualizando ficha {}", id);

        return ResponseEntity.ok(fichaServ.editFicha(id, ficha));
    }
}