package manitasUnidas.denuncias.controller;

import manitasUnidas.denuncias.model.Denuncia;
import manitasUnidas.denuncias.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService service;

    @GetMapping
    public List<Denuncia> listar() {
        return service.listarTodas();
    }

    @PostMapping
    public ResponseEntity<Denuncia> crear(@RequestBody Denuncia denuncia) {
        return new ResponseEntity<>(service.guardar(denuncia), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> actualizarEstado(@PathVariable Long id, @RequestBody Denuncia denunciaDetalles) {
        // utilizamos el service para buscar, cambiar el estado y guardar
        Denuncia denunciaActualizada = service.actualizarEstado(id, denunciaDetalles.getEstado());
        return ResponseEntity.ok(denunciaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}