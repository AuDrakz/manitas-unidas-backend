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
@RequestMapping("/api/blacklist")
public class BlackListController {

    @Autowired
    private BlackListService service;

    @GetMapping
    public ResponseEntity<List<BlackList>> listar() {
        List<BlackList> lista = service.obtenerTodos();
        return ResponseEntity.ok(lista);
    }
    
    // @GetMapping
    // public List<BlackList> listar() {
    //     return service.obtenerTodos();
    // }

    @GetMapping("/{id}")
    public ResponseEntity<BlackList> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    
    // En BlackListController.java
    @GetMapping("/verificar/{rut}")
    public ResponseEntity<Boolean> estaBloqueado(@PathVariable String rut) {
        try {
            service.buscarPorRut(rut); // Si lo encuentra, significa que está en la lista negra
            return ResponseEntity.ok(true); 
        } catch (Exception e) {
            return ResponseEntity.ok(false); // Si no lo encuentra, no está bloqueado
        }
    }

    // En BlackListController.java
    // @GetMapping("/verificar/{rut}")
    // public boolean estaBloqueado(@PathVariable String rut) {
    //     try {
    //         service.buscarPorRut(rut); // Si lo encuentra, lanza éxito
    //         return true; 
    //     } catch (Exception e) {
    //         return false; // Si no lo encuentra (excepción), no está bloqueado
    //     }
    // }

    @PostMapping
    public ResponseEntity<BlackList> agregar(@Valid @RequestBody BlackList registro) {
        return new ResponseEntity<>(service.bloquearUsuario(registro), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.desbloquear(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlackList> actualizar(@PathVariable Long id, @RequestBody BlackList blackList) {
        BlackList actualizado = service.actualizarBlackList(id, blackList);
        return ResponseEntity.ok(actualizado);
    }
}
