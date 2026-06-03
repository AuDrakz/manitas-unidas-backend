package manitasUnidas.mascotas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.service.MascotaService;

@Slf4j   
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    // GET /api/mascotas
    @GetMapping
    public List<Mascota> listar() {
        log.info("[MascotaController] GET /api/mascotas - listar todas");
        return mascotaService.obtenerTodas();
    }

    // GET /api/mascotas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@PathVariable Long id) {
        log.info("[MascotaController] GET /api/mascotas/{}", id);
        return ResponseEntity.ok(mascotaService.buscarPorId(id));
    }

    // POST /api/mascotas  --  ahora recibe MascotaRequestDTO, no la entidad
    @PostMapping
    public ResponseEntity<Mascota> guardar(@Valid @RequestBody MascotaRequestDTO dto) {
        log.info("[MascotaController] POST /api/mascotas - nombre={}", dto.getNombre());
        Mascota nuevaMascota = mascotaService.registrarMascota(dto);
        return new ResponseEntity<>(nuevaMascota, HttpStatus.CREATED);
    }

    // PUT /api/mascotas/{id}  --  ahora recibe MascotaRequestDTO, no la entidad
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizar(@PathVariable Long id,
                                              @Valid @RequestBody MascotaRequestDTO dto) {
        log.info("[MascotaController] PUT /api/mascotas/{}", id);
        Mascota actualizada = mascotaService.actualizarMascota(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/mascotas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("[MascotaController] DELETE /api/mascotas/{}", id);
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/mascotas/existe/{id}  -- usado por ms-solicitud via Feign
    @GetMapping("/existe/{id}")
    public boolean verificarExistencia(@PathVariable Long id) {
        log.info("[MascotaController] Verificando existencia de mascota ID={}", id);
        try {
            mascotaService.buscarPorId(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // GET /api/mascotas/estado/{id}
    @GetMapping("/estado/{id}")
    public ResponseEntity<String> obtenerEstado(@PathVariable Long id) {
        log.info("[MascotaController] Consultando estado de mascota ID={}", id);
        Mascota m = mascotaService.buscarPorId(id);
        return ResponseEntity.ok(m.getEstado());
    }
}