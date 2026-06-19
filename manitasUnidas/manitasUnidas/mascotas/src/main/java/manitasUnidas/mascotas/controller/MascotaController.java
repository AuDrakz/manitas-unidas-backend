package manitasUnidas.mascotas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import manitasUnidas.mascotas.dto.ApiResponse;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.dto.MascotaResponseDTO;
import manitasUnidas.mascotas.service.MascotaService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Tag(
    name = " Mascotas",
    description = "Operaciones relacionadas con gestión de mascotas disponibles para adopción"
)
@Slf4j
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    private EntityModel<MascotaResponseDTO> toModel(MascotaResponseDTO dto) {

        return EntityModel.of(
                dto,
                linkTo(methodOn(MascotaController.class)
                        .buscarPorId(dto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(MascotaController.class)
                        .listar())
                        .withRel("todas-las-mascotas"),

                linkTo(methodOn(MascotaController.class)
                        .obtenerEstado(dto.getId()))
                        .withRel("estado"),

                linkTo(methodOn(MascotaController.class)
                        .eliminar(dto.getId()))
                        .withRel("eliminar")
        );
    }

    @Operation(summary = "Listar todas las mascotas")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<MascotaResponseDTO>>>> listar() {

        log.info("Listando todas las mascotas");

        List<EntityModel<MascotaResponseDTO>> mascotas =
                mascotaService.obtenerTodas()
                        .stream()
                        .map(this::toModel)
                        .toList();

        CollectionModel<EntityModel<MascotaResponseDTO>> collection =
                CollectionModel.of(
                        mascotas,
                        linkTo(methodOn(MascotaController.class)
                                .listar())
                                .withSelfRel()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Mascotas obtenidas correctamente",
                        collection
                )
        );
    }

    @Operation(summary = "Buscar mascota por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<MascotaResponseDTO>>> buscarPorId(
            @PathVariable Long id) {

        log.info("Buscando mascota id={}", id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Mascota encontrada correctamente",
                        toModel(mascotaService.buscarPorId(id))
                )
        );
    }

    @Operation(summary = "Registrar nueva mascota")
    @PostMapping
    public ResponseEntity<ApiResponse<EntityModel<MascotaResponseDTO>>> guardar(
            @Valid @RequestBody MascotaRequestDTO dto) {

        log.info("Creando mascota nombre={}", dto.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                201,
                                "Mascota registrada correctamente",
                                toModel(mascotaService.registrarMascota(dto))
                        )
                );
    }

    @Operation(summary = "Actualizar mascota")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<MascotaResponseDTO>>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequestDTO dto) {

        log.info("Actualizando mascota id={}", id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Mascota actualizada correctamente",
                        toModel(mascotaService.actualizarMascota(id, dto))
                )
        );
    }

    @Operation(summary = "Eliminar mascota")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long id) {

        log.info("Eliminando mascota id={}", id);

        mascotaService.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Mascota eliminada correctamente",
                        null
                )
        );
    }

    @Operation(summary = "Verificar existencia (interno Feign)")
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> verificarExistencia(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Consulta realizada correctamente",
                        mascotaService.existePorId(id)
                )
        );
    }

    @Operation(summary = "Obtener estado (interno Feign)")
    @GetMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<String>> obtenerEstado(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Estado obtenido correctamente",
                        mascotaService.obtenerEstado(id)
                )
        );
    }
}