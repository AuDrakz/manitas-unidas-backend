package manitasUnidas.solicitud.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para comunicarse con ms-usuarios.
 * Verifica que el adoptante exista antes de crear la solicitud.
 */
@FeignClient(name = "ms-usuarios")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/existe/{id}")
    boolean existeUsuario(@PathVariable("id") Long id);
}