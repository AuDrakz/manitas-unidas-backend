package manitasUnidas.mascotas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios") // nombre microservicios 
public interface UsuarioClient {

    @GetMapping("/api/usuarios/existe/{id}")
    boolean verificarExistencia(@PathVariable("id") Long id);
}