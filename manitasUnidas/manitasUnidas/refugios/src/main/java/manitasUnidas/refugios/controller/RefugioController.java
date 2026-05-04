package manitasUnidas.refugios.controller;

import manitasUnidas.refugios.model.Refugio;
import manitasUnidas.refugios.service.RefugioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refugios")
public class RefugioController {

    @Autowired
    private RefugioService refugioServ;

    // GUARDAR
    @PostMapping("/crear")
    public String guardar(@Valid @RequestBody Refugio refugio) {
        refugioServ.guardarRefugio(refugio);
        return "Refugio registrado exitosamente en el sistema.";
    }

    // LISTAR
    @GetMapping("/listar")
    public List<Refugio> listar() {
        return refugioServ.listarTodos();
    }

    // BUSCAR POR ID
    @GetMapping("/buscar/{id}")
    public Refugio buscarPorId(@PathVariable Long id) {
        return refugioServ.buscarPorId(id);
    }

    // BUSCAR POR NOMBRE
    @GetMapping("/buscar/nombre/{nombre}")
    public Refugio buscarPorNombre(@PathVariable String nombre) {
        return refugioServ.buscarPorNombre(nombre);
    }

    // MÉTODO: BUSCAR POR RESPONSABLE
    @GetMapping("/buscar/responsable/{responsable}")
    public List<Refugio> buscarPorResponsable(@PathVariable String responsable) {
        return refugioServ.buscarPorResponsable(responsable);
    }

    // BUSCAR POR COMUNA / DIRECCIÓN EXACTA
    @GetMapping("/buscar/direccion/{direccion}")
    public List<Refugio> buscarPorDireccion(@PathVariable String direccion) {
        return refugioServ.buscarPorDireccion(direccion);
    }

    // BUSCAR REFUGIOS QUE NO ESTÉN AL MÁXIMO (DISPONIBILIDAD)
    @GetMapping("/disponibles")
    public List<Refugio> buscarDisponibles() {
        return refugioServ.buscarConDisponibilidad();
    }

    // INDICAR CANTIDAD DE CUPOS DISPONIBLES
    @GetMapping("/cupos/{id}")
    public String verCupos(@PathVariable Long id) {
        Integer cupos = refugioServ.obtenerCuposDisponibles(id);
        return "El refugio seleccionado cuenta con " + cupos + " cupos disponibles.";
    }

    // ELIMINAR
    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        refugioServ.eliminarRefugio(id);
        return "El refugio ha sido eliminado correctamente.";
    }

    // EDITAR / ACTUALIZAR
    @PutMapping("/editar")
    public String editar(@RequestBody Refugio refugio) {
        refugioServ.actualizarRefugio(refugio);
        return "Los datos del refugio han sido actualizados.";
    }
}
