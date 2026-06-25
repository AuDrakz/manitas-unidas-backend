package manitasUnidas.refugios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import manitasUnidas.refugios.dto.RefugioRequestDTO;
import manitasUnidas.refugios.dto.RefugioResponseDTO;
import manitasUnidas.refugios.service.RefugioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequestMapping("/api/refugios")
@Tag(name = "Refugios", description = "Gestión de refugios de animales")
public class RefugioController {

    // Declaración explícita del Logger compatible con cualquier versión de Java
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RefugioController.class);

    @Autowired
    private RefugioService refugioServ;

    private EntityModel<RefugioResponseDTO> toModel(RefugioResponseDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(RefugioController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(RefugioController.class).listar()).withRel("todos-los-refugios"),
            linkTo(methodOn(RefugioController.class).buscarDisponibles()).withRel("disponibles"),
            linkTo(methodOn(RefugioController.class).verCupos(dto.getId())).withRel("cupos"),
            linkTo(methodOn(RefugioController.class).eliminar(dto.getId())).withRel("eliminar")
        );
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo refugio")
    public ResponseEntity<EntityModel<RefugioResponseDTO>> crear(
            @Valid @RequestBody RefugioRequestDTO dto) {
        log.info("Solicitud para crear refugio");
        EntityModel<RefugioResponseDTO> model = toModel(refugioServ.guardarRefugio(dto));
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los refugios")
    public CollectionModel<EntityModel<RefugioResponseDTO>> listar() {
        log.info("Solicitud para listar refugios");
        List<EntityModel<RefugioResponseDTO>> refugios = refugioServ.listarTodos()
                .stream().map(this::toModel).toList();
        return CollectionModel.of(refugios,
                linkTo(methodOn(RefugioController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar refugio por ID")
    public EntityModel<RefugioResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Solicitud para buscar refugio ID: {}", id);
        return toModel(refugioServ.buscarPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar refugio por nombre")
    public EntityModel<RefugioResponseDTO> buscarPorNombre(@PathVariable String nombre) {
        log.info("Solicitud para buscar refugio nombre: {}", nombre);
        RefugioResponseDTO dto = refugioServ.buscarPorNombre(nombre);
        return toModel(dto);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Listar refugios con disponibilidad")
    public CollectionModel<EntityModel<RefugioResponseDTO>> buscarDisponibles() {
        log.info("Solicitud para listar refugios disponibles");
        List<EntityModel<RefugioResponseDTO>> disponibles = refugioServ.buscarConDisponibilidad()
                .stream().map(this::toModel).toList();
        return CollectionModel.of(disponibles,
                linkTo(methodOn(RefugioController.class).buscarDisponibles()).withSelfRel());
    }

    @GetMapping("/cupos/{id}")
    @Operation(summary = "Consultar cupos disponibles")
    public ResponseEntity<String> verCupos(@PathVariable Long id) {
        log.info("Solicitud para consultar cupos refugio ID: {}", id);
        Integer cupos = refugioServ.obtenerCuposDisponibles(id);
        return ResponseEntity.ok("El refugio cuenta con " + cupos + " cupos disponibles.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar refugio")
    public EntityModel<RefugioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RefugioRequestDTO dto) {
        log.info("Solicitud para actualizar refugio ID: {}", id);
        return toModel(refugioServ.actualizarRefugio(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar refugio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Solicitud para eliminar refugio ID: {}", id);
        refugioServ.eliminarRefugio(id);
        return ResponseEntity.noContent().build();
    }
}