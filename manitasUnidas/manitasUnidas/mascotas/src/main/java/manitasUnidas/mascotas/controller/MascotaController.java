package manitasUnidas.mascotas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.service.MascotaService;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {
    @Autowired
    private MascotaService mascotaService;

    //obtener todas las mascotas
    @GetMapping
    public List<Mascota> listar() {
        return mascotaService.obtenerTodas();
    }

    // obtener mascota por id
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.buscarPorId(id));
    }

    // registrar nueva mascota
    @PostMapping
    public ResponseEntity<Mascota> guardar (@Valid @RequestBody Mascota mascota) {
        Mascota nuevaMascota = mascotaService.registrarMascota(mascota);
        return new ResponseEntity<>(nuevaMascota,HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizar(@PathVariable Long id, @RequestBody Mascota mascota) {
        // Llamamos al service para procesar la actualización
        Mascota actualizada = mascotaService.actualizarMascota(id, mascota);
        return ResponseEntity.ok(actualizada);
    }

    // eliminar una mascota
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // metodo para verificar si la mascota existe (lo usará SolicitudClient)
    // @GetMapping("/existe/{id}")
    // public Boolean verificarExistencia(@PathVariable Long id) {
    //     try {
    //         mascotaService.buscarPorId(id);
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }

    // metodo para verificar si la mascota existe (lo usará SolicitudClient)
    @GetMapping("/existe/{id}")
    public ResponseEntity<Boolean> verificarExistencia(@PathVariable Long id) {
        try {
            mascotaService.buscarPorId(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    // metodo para obtener solo el estado de la mascota
    @GetMapping("/estado/{id}")
    public ResponseEntity<String> obtenerEstado(@PathVariable Long id) {
        Mascota m = mascotaService.buscarPorId(id);
        return ResponseEntity.ok(m.getEstado()); 
    }

    
}
