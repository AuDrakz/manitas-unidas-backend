package manitasUnidas.mascotas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.dto.MascotaResponseDTO;
import manitasUnidas.mascotas.service.MascotaService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api/mascotas")
@Tag(name = "Mascotas", description = "Gestión de mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponseDTO>> listar() {
        log.info("Listando todas las mascotas");
        return ResponseEntity.ok(mascotaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando mascota id={}", id);
        return ResponseEntity.ok(mascotaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> guardar(@Valid @RequestBody MascotaRequestDTO dto) {
        log.info("Creando mascota nombre={}", dto.getNombre());
        return new ResponseEntity<>(mascotaService.registrarMascota(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO dto) {
        log.info("Actualizando mascota id={}", id);
        return ResponseEntity.ok(mascotaService.actualizarMascota(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando mascota id={}", id);
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> verificarExistencia(@PathVariable Long id) {
        log.info("Verificando existencia id={}", id);
        return ResponseEntity.ok(mascotaService.existePorId(id));
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<String> obtenerEstado(@PathVariable Long id) {
        log.info("Consultando estado id={}", id);
        return ResponseEntity.ok(mascotaService.obtenerEstado(id));
    }
}