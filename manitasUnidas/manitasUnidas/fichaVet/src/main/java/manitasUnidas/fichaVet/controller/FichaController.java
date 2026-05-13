package manitasUnidas.fichaVet.controller;

import manitasUnidas.fichaVet.model.Ficha;
import manitasUnidas.fichaVet.service.FichaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichavet")
public class FichaController {

    @Autowired
    private FichaService fichaServ;

    // 1. CREAR
    @PostMapping
    public ResponseEntity<Ficha> crearFicha(@Valid @RequestBody Ficha ficha) {

        fichaServ.saveFicha(ficha);

        return ResponseEntity.status(201).body(ficha);
    }

    // 2. TRAER TODAS
    @GetMapping
    public List<Ficha> traerFichas() {
        return fichaServ.getFichas();
    }

    // 3. TRAER POR ID
    @GetMapping("/{id}")
    public Ficha traerFicha(@PathVariable Long id) {
        return fichaServ.findFicha(id);
    }

    // 4. HISTORIAL POR MASCOTA
    @GetMapping("/mascota/{idMascota}")
    public List<Ficha> historialPorMascota(@PathVariable Integer idMascota) {
        return fichaServ.findByMascota(idMascota);
    }

    // 5. BUSCAR POR VETERINARIO
    @GetMapping("/veterinario/{rut}")
    public List<Ficha> buscarPorRut(@PathVariable String rut) {
        return fichaServ.findByRutVeterinario(rut);
    }

    // 6. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFicha(@PathVariable Long id) {

        fichaServ.deleteFicha(id);

        return ResponseEntity.noContent().build();
    }

    // 7. EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<Ficha> editarFicha(@PathVariable Long id,@Valid @RequestBody Ficha ficha) {

        Ficha fichaActualizada = fichaServ.editFicha(id, ficha);

        return ResponseEntity.ok(fichaActualizada);
    }
}