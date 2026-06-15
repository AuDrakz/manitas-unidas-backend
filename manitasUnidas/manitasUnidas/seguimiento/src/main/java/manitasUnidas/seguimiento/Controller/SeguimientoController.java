package manitasUnidas.seguimiento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.seguimiento.service.SeguimientoService;
import manitasUnidas.seguimiento.dto.SeguimientoRequestDTO;
import manitasUnidas.seguimiento.dto.SeguimientoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

@RestController
@RequestMapping("/api/seguimientos")
@Tag(name = "Seguimientos", description = "Gestion de seguimientos clinicos veterinarios")
@Slf4j
public class SeguimientoController {

    @Autowired
    private SeguimientoService service;

    private EntityModel<SeguimientoResponseDTO> toModel(SeguimientoResponseDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(SeguimientoController.class).obtenerPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(SeguimientoController.class).listarSeguimientos()).withRel("todos-los-seguimientos"),
            linkTo(methodOn(SeguimientoController.class).obtenerHistorialClinico(dto.getFichaVetId())).withRel("historial-ficha"),
            linkTo(methodOn(SeguimientoController.class).eliminarSeguimiento(dto.getId())).withRel("eliminar")
        );
    }

    @PostMapping
    @Operation(summary = "Crear seguimiento")
    public ResponseEntity<EntityModel<SeguimientoResponseDTO>> crearSeguimiento(
            @Valid @RequestBody SeguimientoRequestDTO dto) {
        log.info("Solicitud para crear seguimiento fichaVetId={}", dto.getFichaVetId());
        return new ResponseEntity<>(toModel(service.crearSeguimiento(dto)), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar seguimientos")
    public CollectionModel<EntityModel<SeguimientoResponseDTO>> listarSeguimientos() {
        log.info("Solicitud para listar seguimientos");
        List<EntityModel<SeguimientoResponseDTO>> lista = service.listarSeguimientos().stream().map(this::toModel).toList();
        return CollectionModel.of(lista, linkTo(methodOn(SeguimientoController.class).listarSeguimientos()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar seguimiento por ID")
    public EntityModel<SeguimientoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Solicitud para buscar seguimiento ID={}", id);
        return toModel(service.obtenerPorId(id));
    }

    @GetMapping("/historial/{fichaVetId}")
    @Operation(summary = "Obtener historial clinico por ficha")
    public CollectionModel<EntityModel<SeguimientoResponseDTO>> obtenerHistorialClinico(
            @PathVariable Long fichaVetId) {
        log.info("Solicitud historial fichaVetId={}", fichaVetId);
        List<EntityModel<SeguimientoResponseDTO>> lista = service.obtenerHistorialClinico(fichaVetId)
                .stream().map(this::toModel).toList();
        return CollectionModel.of(lista,
            linkTo(methodOn(SeguimientoController.class).obtenerHistorialClinico(fichaVetId)).withSelfRel());
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar seguimientos por estado")
    public CollectionModel<EntityModel<SeguimientoResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        log.info("Solicitud busqueda estado={}", estado);
        List<EntityModel<SeguimientoResponseDTO>> lista = service.buscarPorEstado(estado)
                .stream().map(this::toModel).toList();
        return CollectionModel.of(lista,
            linkTo(methodOn(SeguimientoController.class).buscarPorEstado(estado)).withSelfRel());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar seguimiento")
    public EntityModel<SeguimientoResponseDTO> actualizarSeguimiento(
            @PathVariable Long id,
            @Valid @RequestBody SeguimientoRequestDTO dto) {
        log.info("Solicitud actualizacion seguimiento ID={}", id);
        return toModel(service.actualizarSeguimiento(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar seguimiento")
    public ResponseEntity<Void> eliminarSeguimiento(@PathVariable Long id) {
        log.warn("Solicitud eliminacion seguimiento ID={}", id);
        service.eliminarSeguimiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existe/{fichaVetId}")
    @Operation(summary = "Verificar existencia de seguimiento (interno)")
    public ResponseEntity<Boolean> existeFichaVet(@PathVariable Long fichaVetId) {
        log.info("Validando existencia fichaVetId={}", fichaVetId);
        return ResponseEntity.ok(service.existeFichaVet(fichaVetId));
    }
}