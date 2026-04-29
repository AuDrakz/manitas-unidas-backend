package manitasUnidas.fichaVet.controller;

import manitasUnidas.fichaVet.model.Ficha;
import manitasUnidas.fichaVet.service.FichaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichavet")
public class FichaController {

    @Autowired
    private FichaService fichaServ;

    // 1. CREAR: Registrar una nueva consulta médica
    @PostMapping("/crear")
    public String crearFicha(@Valid @RequestBody Ficha ficha) {
        fichaServ.saveFicha(ficha);
        return "La ficha médica ha sido guardada correctamente en el historial.";
    }

    // 2. TRAER TODAS: Ver todos los registros del albergue
    @GetMapping("/traer")
    public List<Ficha> traerFichas() {
        return fichaServ.getFichas();
    }

    // 3. TRAER POR ID: Ver el detalle de una ficha específica
    @GetMapping("/traer/{id}")
    public Ficha traerFicha(@PathVariable Long id) {
        return fichaServ.findFicha(id);
    }

    // 4. RESPALDO POR MASCOTA: Ver todo el historial de un animal específico
    @GetMapping("/mascota/{idMascota}")
    public List<Ficha> historialPorMascota(@PathVariable Integer idMascota) {
        return fichaServ.findByMascota(idMascota);
    }

    // 5. BUSCAR POR VETERINARIO: Ver fichas según el RUT del médico
    @GetMapping("/veterinario/{rut}")
    public List<Ficha> buscarPorRut(@PathVariable String rut) {
        return fichaServ.findByRutVeterinario(rut);
    }

    // 6. BORRAR: Eliminar un registro por su ID
    @DeleteMapping("/borrar/{id}")
    public String eliminarFicha(@PathVariable Long id) {
        fichaServ.deleteFicha(id);
        return "Registro médico eliminado con éxito.";
    }

    // 7. EDITAR: Modificar datos de una ficha existente
    @PutMapping("/editar")
    public Ficha editarFicha(@RequestBody Ficha ficha) {
        fichaServ.editFicha(ficha);
        return fichaServ.findFicha(ficha.getId());
    }
}