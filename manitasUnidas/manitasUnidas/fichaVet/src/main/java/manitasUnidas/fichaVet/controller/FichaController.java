package manitasUnidas.fichaVet.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.fichaVet.dto.FichaRequestDTO;
import manitasUnidas.fichaVet.dto.FichaResponseDTO;
import manitasUnidas.fichaVet.service.FichaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@Tag(
    name = "Ficha Veterinaria",
    description = "Operaciones relacionadas con fichas médicas de mascotas"
)


@Slf4j
@RestController
@RequestMapping("/api/fichavet")
public class FichaController {


    private final FichaService fichaServ;

    public FichaController(FichaService fichaServ) {
        this.fichaServ = fichaServ;
    }
    

    // CREAR
    @Operation(
    summary = "Crear ficha veterinaria",
    description = "Registra una nueva ficha médica para una mascota")

    @PostMapping
    public ResponseEntity<FichaResponseDTO> crearFicha(
            @Valid @RequestBody FichaRequestDTO dto) {

        log.info("Creando ficha veterinaria para mascota {}", dto.getIdMascota());

        return ResponseEntity.status(201)
                .body(fichaServ.saveFicha(dto));
    }

    // TRAER TODAS
    @Operation(
    summary = "Listar fichas",
    description = "Obtiene todas las fichas veterinarias registradas")

    @GetMapping
    public ResponseEntity<List<FichaResponseDTO>> traerFichas() {

        log.info("Obteniendo todas las fichas veterinarias");

        return ResponseEntity.ok(fichaServ.getFichas());
    }

    // TRAER POR ID
    @Operation(
    summary = "Buscar ficha por ID",
    description = "Obtiene una ficha veterinaria utilizando su identificador")

    @GetMapping("/{id}")
    public ResponseEntity<FichaResponseDTO> traerFicha(@PathVariable Long id) {

        log.info("Buscando ficha {}", id);

        return ResponseEntity.ok(fichaServ.findFicha(id));
    }

    // HISTORIAL POR MASCOTA
    @Operation(
    summary = "Historial clínico",
    description = "Obtiene todas las fichas asociadas a una mascota")

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<FichaResponseDTO>> historialPorMascota(
            @PathVariable Long idMascota) {

        log.info("Consultando historial de mascota {}", idMascota);

        return ResponseEntity.ok(fichaServ.findByMascota(idMascota));
    }

    // BUSCAR POR VETERINARIO
    @Operation(
    summary = "Buscar por veterinario",
    description = "Obtiene las fichas registradas por un veterinario")
    
    @GetMapping("/veterinario/{rut}")
    public ResponseEntity<List<FichaResponseDTO>> buscarPorRut(
            @PathVariable String rut) {

        log.info("Consultando fichas del veterinario {}", rut);

        return ResponseEntity.ok(fichaServ.findByRutVeterinario(rut));
    }

    // ELIMINAR
    @Operation(
    summary = "Eliminar ficha",
    description = "Elimina una ficha veterinaria por ID")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFicha(@PathVariable Long id) {

        log.warn("Eliminando ficha {}", id);

        fichaServ.deleteFicha(id);

        return ResponseEntity.noContent().build();
    }

    // EDITAR
    @Operation(
    summary = "Actualizar ficha",
    description = "Modifica una ficha veterinaria existente")
    
    @PutMapping("/{id}")
    public ResponseEntity<FichaResponseDTO> editarFicha(
            @PathVariable Long id,
            @Valid @RequestBody FichaRequestDTO dto) {

        log.info("Actualizando ficha {}", id);

        return ResponseEntity.ok(fichaServ.editFicha(id, dto));
    }
}