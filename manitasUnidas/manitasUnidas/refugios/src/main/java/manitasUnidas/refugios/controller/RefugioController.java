package manitasUnidas.refugios.controller;

import manitasUnidas.refugios.model.Refugio;
import manitasUnidas.refugios.service.RefugioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refugios")
public class RefugioController {

    @Autowired
    private RefugioService refugioServ;

    // CREAR: POST /api/refugios
    @PostMapping
    public Refugio guardar(@Valid @RequestBody Refugio refugio) {
        return refugioServ.guardarRefugio(refugio); // Aquí ya retorna el JSON
    }

    // LISTAR TODOS: GET /api/refugios
    @GetMapping
    public List<Refugio> listar() {
        return refugioServ.listarTodos();
    }

    // BUSCAR POR ID: GET /api/refugios/{id}
    @GetMapping("/{id}")
    public Refugio buscarPorId(@PathVariable Long id) {
        return refugioServ.buscarPorId(id);
    }

    // BUSCAR POR NOMBRE: GET /api/refugios/nombre/{nombre}
    @GetMapping("/nombre/{nombre}")
    public Refugio buscarPorNombre(@PathVariable String nombre) {
        return refugioServ.buscarPorNombre(nombre);
    }

    // DISPONIBILIDAD: GET /api/refugios/disponibles
    @GetMapping("/disponibles")
    public List<Refugio> buscarDisponibles() {
        return refugioServ.buscarConDisponibilidad();
    }

    // CUPOS: GET /api/refugios/cupos/{id}
    @GetMapping("/cupos/{id}")
    public String verCupos(@PathVariable Long id) {
        Integer cupos = refugioServ.obtenerCuposDisponibles(id);
        return "El refugio cuenta con " + cupos + " cupos disponibles.";
    }


    // ELIMINAR: DELETE /api/refugios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        refugioServ.eliminarRefugio(id);
        return ResponseEntity.noContent().build(); 
    }
    // @DeleteMapping("/{id}")
    // public String eliminar(@PathVariable Long id) {
    //     refugioServ.eliminarRefugio(id);
    //     return "El refugio ha sido eliminado correctamente.";
    // }

    // EDITAR: PUT /api/refugios/{id}
    @PutMapping("/{id}")
    public Refugio editar(@PathVariable Long id, @RequestBody Refugio refugio) {
        refugio.setId(id);
        return refugioServ.actualizarRefugio(refugio);
    }

    // COMUNICACIÓN ENTRE MICROSERVICIOS: GET /api/refugios/existe/{id}
    @GetMapping("/existe/{id}")
    public boolean verificarExistencia(@PathVariable Long id) {
        try {
            return refugioServ.buscarPorId(id) != null;
        } catch (Exception e) {
            return false;
        }
    }
}