package manitasUnidas.mascotas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.dto.MascotaResponseDTO;
import manitasUnidas.mascotas.service.MascotaService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Mascotas", description = "Gestión de mascotas disponibles para adopción")
@Slf4j
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    private EntityModel<MascotaResponseDTO> toModel(MascotaResponseDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(MascotaController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(MascotaController.class).listar()).withRel("todas-las-mascotas"),
            linkTo(methodOn(MascotaController.class).obtenerEstado(dto.getId())).withRel("estado"),
            linkTo(methodOn(MascotaController.class).eliminar(dto.getId())).withRel("eliminar")
        );
    }

    @Operation(summary = "Listar todas las mascotas")
    @GetMapping
    public CollectionModel<EntityModel<MascotaResponseDTO>> listar() {
        log.info("Listando todas las mascotas");

        List<EntityModel<MascotaResponseDTO>> mascotas = mascotaService.obtenerTodas()
                .stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaController.class).listar()).withSelfRel());
    }

    @Operation(summary = "Buscar mascota por ID")
    @GetMapping("/{id}")
    public EntityModel<MascotaResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando mascota id={}", id);
        return toModel(mascotaService.buscarPorId(id));
    }

    @Operation(summary = "Registrar nueva mascota")
    @PostMapping
    public ResponseEntity<EntityModel<MascotaResponseDTO>> guardar(
            @Valid @RequestBody MascotaRequestDTO dto) {
        log.info("Creando mascota nombre={}", dto.getNombre());
        return new ResponseEntity<>(toModel(mascotaService.registrarMascota(dto)), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar mascota")
    @PutMapping("/{id}")
    public EntityModel<MascotaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO dto) {
        log.info("Actualizando mascota id={}", id);
        return toModel(mascotaService.actualizarMascota(id, dto));
    }

    @Operation(summary = "Eliminar mascota")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando mascota id={}", id);
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar existencia (interno Feign)")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> verificarExistencia(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.existePorId(id));
    }

    @Operation(summary = "Obtener estado (interno Feign)")
    @GetMapping("/{id}/estado")
    public ResponseEntity<String> obtenerEstado(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.obtenerEstado(id));
    }
}