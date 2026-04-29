package manitasUnidas.blackList.Controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import manitasUnidas.blackList.Model.BlackList;
import manitasUnidas.blackList.Service.BlackListService;


@RestController
@RequestMapping("/api/lista-negra")
public class BlackListController {

    @Autowired
    private BlackListService service;

    @GetMapping
    public List<BlackList> listar() {
        return service.obtenerTodos();
    }

    // Endpoint clave para el servicio de Adopciones
    @GetMapping("/verificar/{rut}")
    public ResponseEntity<Boolean> estaBloqueado(@PathVariable String rut) {
        try {
            service.buscarPorRut(rut);
            return ResponseEntity.ok(true); // Si lo encuentra, está bloqueado
        } catch (Exception e) {
            return ResponseEntity.ok(false); // Si no, está limpio
        }
    }

    @PostMapping
    public ResponseEntity<BlackList> agregar(@Valid @RequestBody BlackList registro) {
        return new ResponseEntity<>(service.bloquearUsuario(registro), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.desbloquear(id);
        return ResponseEntity.noContent().build();
    }
}
