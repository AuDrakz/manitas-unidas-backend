package manitasUnidas.blackList.Controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import manitasUnidas.blackList.Model.BlackList;
import manitasUnidas.blackList.Service.BlackListService;

// Importaciones oficiales de OpenAPI/Swagger según la guía Duoc
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/blacklist")
@Tag(name = "Módulo de Lista Negra (Blacklist)", description = "Endpoints para controlar la restricción y el bloqueo de acceso de usuarios en el sistema.")
public class BlackListController {

    @Autowired
    private BlackListService service;

    @Operation(
        summary = "📋 Listar registros de la Lista Negra", 
        description = "Retorna un listado con todas las fichas de usuarios que se encuentran bloqueados actualmente."
    )
    @ApiResponse(responseCode = "200", description = "¡Éxito! Lista de bloqueos obtenida de forma correcta.")
    @GetMapping
    public ResponseEntity<List<BlackList>> listar() {
        // CORREGIDO: Llama exactamente al método de tu servicio sin errores de tipeo
        List<BlackList> lista = service.obtenerTodos(); 
        return ResponseEntity.ok(lista);
    }

    @Operation(
        summary = "🔍 Buscar un bloqueo por su ID", 
        description = "Ingresa el identificador numérico único (ID) para revisar el motivo y detalles de un bloqueo específico."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "¡Registro de bloqueo localizado con éxito!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlackList.class))),
        @ApiResponse(responseCode = "404", description = "No encontrado: El ID consultado no figura en la lista negra.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BlackList> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(
        summary = "🛡️ Verificar si un RUT está bloqueado", 
        description = "Consulta rápida para saber si un usuario tiene el acceso restringido ingresando su RUT. Devuelve 'true' si está bloqueado o 'false' si tiene permitido el paso."
    )
    @ApiResponse(responseCode = "200", description = "Consulta completada. Retorna un valor verdadero o falso (true/false).")
    @GetMapping("/verificar/{rut}")
    public ResponseEntity<Boolean> estaBloqueado(@PathVariable String rut) {
        try {
            service.buscarPorRut(rut); 
            return ResponseEntity.ok(true); 
        } catch (Exception e) {
            return ResponseEntity.ok(false); 
        }
    }

    @Operation(
        summary = "🚫 Agregar un usuario a la Lista Negra", 
        description = "Registra un nuevo bloqueo ingresando los datos correspondientes en el formulario. El RUT y el motivo son obligatorios."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "¡Usuario agregado a la lista negra exitosamente!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlackList.class))),
        @ApiResponse(responseCode = "400", description = "Error de envío: Los datos del formulario contienen inconsistencias o faltan campos obligatorios.")
    })
    @PostMapping
    public ResponseEntity<BlackList> agregar(@Valid @RequestBody BlackList registro) {
        return new ResponseEntity<>(service.bloquearUsuario(registro), HttpStatus.CREATED);
    }

    @Operation(
        summary = "🔄 Modificar los datos de un bloqueo", 
        description = "Permite actualizar el motivo, la fecha o la información de un registro existente utilizando su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "¡Registro de lista negra actualizado con éxito!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlackList.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo modificar: El ID especificado no existe en el sistema.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BlackList> actualizar(@PathVariable Long id, @RequestBody BlackList blackList) {
        BlackList actualizado = service.actualizarBlackList(id, blackList);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(
        summary = "🔓 Quitar de la Lista Negra (Desbloquear)", 
        description = "Elimina permanentemente la restricción de un usuario en el sistema mediante su ID para restablecer su acceso normal."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "¡Usuario desbloqueado con éxito! Registro removido correctamente."),
        @ApiResponse(responseCode = "404", description = "No se pudo remover: El ID especificado no fue encontrado en los registros.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.desbloquear(id);
        return ResponseEntity.noContent().build();
    }
}