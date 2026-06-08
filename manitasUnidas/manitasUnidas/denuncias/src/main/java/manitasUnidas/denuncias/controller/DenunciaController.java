package manitasUnidas.denuncias.controller;

import manitasUnidas.denuncias.model.Denuncia;
import manitasUnidas.denuncias.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Denuncias", description = "Gestión de denuncias por maltrato o abandono animal")
@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService service;

    @Operation(summary = "Listar todas las denuncias", description = "Retorna la lista completa de denuncias registradas")
    @GetMapping
    public List<Denuncia> listar() {
        return service.listarTodas();
    }

    @Operation(summary = "Registrar nueva denuncia", description = "Crea una nueva denuncia por maltrato o abandono animal")
    @PostMapping
    public ResponseEntity<Denuncia> crear(@RequestBody Denuncia denuncia) {
        return new ResponseEntity<>(service.guardar(denuncia), HttpStatus.CREATED);
    }

    @Operation(summary = "Buscar denuncia por ID", description = "Retorna los datos de una denuncia específica")
    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Actualizar estado de denuncia",
               description = "Modifica el estado de una denuncia existente (ej: EN_REVISION, RESUELTA, CERRADA)")
    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> actualizarEstado(@PathVariable Long id,
                                                     @RequestBody Denuncia denunciaDetalles) {
        Denuncia denunciaActualizada = service.actualizarEstado(id, denunciaDetalles.getEstado());
        return ResponseEntity.ok(denunciaActualizada);
    }

    @Operation(summary = "Eliminar denuncia", description = "Elimina una denuncia del sistema por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}