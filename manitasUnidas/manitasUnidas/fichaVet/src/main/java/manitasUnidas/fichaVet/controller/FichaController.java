package manitasUnidas.fichaVet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import manitasUnidas.fichaVet.dto.ApiResponse;
import manitasUnidas.fichaVet.dto.FichaRequestDTO;
import manitasUnidas.fichaVet.dto.FichaResponseDTO;
import manitasUnidas.fichaVet.service.FichaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(
    name = "Ficha Veterinaria",
    description = "Operaciones relacionadas con fichas medicas de mascotas"
)
@Slf4j
@RestController
@RequestMapping("/api/fichavet")
public class FichaController {

    private final FichaService fichaServ;

    public FichaController(FichaService fichaServ) {
        this.fichaServ = fichaServ;
    }

    private EntityModel<FichaResponseDTO> toModel(FichaResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(FichaController.class)
                        .traerFicha(dto.getIdFicha()))
                        .withSelfRel(),

                linkTo(methodOn(FichaController.class)
                        .traerFichas())
                        .withRel("todas-las-fichas"),

                linkTo(methodOn(FichaController.class)
                        .historialPorMascota(dto.getIdMascota()))
                        .withRel("historial-mascota"),

                linkTo(methodOn(FichaController.class)
                        .eliminarFicha(dto.getIdFicha()))
                        .withRel("eliminar")
        );
    }

    @Operation(summary = "Crear ficha veterinaria")
    @PostMapping
    public ResponseEntity<ApiResponse<EntityModel<FichaResponseDTO>>> crearFicha(
            @Valid @RequestBody FichaRequestDTO dto) {

        log.info("Creando ficha veterinaria para mascota {}", dto.getIdMascota());

        EntityModel<FichaResponseDTO> ficha =
                toModel(fichaServ.saveFicha(dto));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        201,
                        "Ficha veterinaria creada correctamente",
                        ficha
                ));
    }

    @Operation(summary = "Listar fichas")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<FichaResponseDTO>>>> traerFichas() {

        log.info("Obteniendo todas las fichas veterinarias");

        List<EntityModel<FichaResponseDTO>> fichas =
                fichaServ.getFichas()
                        .stream()
                        .map(this::toModel)
                        .toList();

        CollectionModel<EntityModel<FichaResponseDTO>> collection =
                CollectionModel.of(
                        fichas,
                        linkTo(methodOn(FichaController.class)
                                .traerFichas())
                                .withSelfRel()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Fichas obtenidas correctamente",
                        collection
                )
        );
    }

    @Operation(summary = "Buscar ficha por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<FichaResponseDTO>>> traerFicha(
            @PathVariable Long id) {

        log.info("Buscando ficha {}", id);

        EntityModel<FichaResponseDTO> ficha =
                toModel(fichaServ.findFicha(id));

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Ficha encontrada correctamente",
                        ficha
                )
        );
    }

    @Operation(summary = "Historial clinico por mascota")
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<FichaResponseDTO>>>> historialPorMascota(
            @PathVariable Long idMascota) {

        log.info("Consultando historial de mascota {}", idMascota);

        List<EntityModel<FichaResponseDTO>> fichas =
                fichaServ.findByMascota(idMascota)
                        .stream()
                        .map(this::toModel)
                        .toList();

        CollectionModel<EntityModel<FichaResponseDTO>> collection =
                CollectionModel.of(
                        fichas,
                        linkTo(methodOn(FichaController.class)
                                .historialPorMascota(idMascota))
                                .withSelfRel()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Historial clínico obtenido correctamente",
                        collection
                )
        );
    }

    @Operation(summary = "Buscar por veterinario")
    @GetMapping("/veterinario/{rut}")
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<FichaResponseDTO>>>> buscarPorRut(
            @PathVariable String rut) {

        log.info("Consultando fichas del veterinario {}", rut);

        List<EntityModel<FichaResponseDTO>> fichas =
                fichaServ.findByRutVeterinario(rut)
                        .stream()
                        .map(this::toModel)
                        .toList();

        CollectionModel<EntityModel<FichaResponseDTO>> collection =
                CollectionModel.of(
                        fichas,
                        linkTo(methodOn(FichaController.class)
                                .buscarPorRut(rut))
                                .withSelfRel()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Fichas obtenidas correctamente",
                        collection
                )
        );
    }

    @Operation(summary = "Actualizar ficha")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<FichaResponseDTO>>> editarFicha(
            @PathVariable Long id,
            @Valid @RequestBody FichaRequestDTO dto) {

        log.info("Actualizando ficha {}", id);

        EntityModel<FichaResponseDTO> ficha =
                toModel(fichaServ.editFicha(id, dto));

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Ficha actualizada correctamente",
                        ficha
                )
        );
    }

    @Operation(summary = "Eliminar ficha")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarFicha(
            @PathVariable Long id) {

        log.warn("Eliminando ficha {}", id);

        fichaServ.deleteFicha(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Ficha eliminada correctamente",
                        null
                )
        );
    }
}