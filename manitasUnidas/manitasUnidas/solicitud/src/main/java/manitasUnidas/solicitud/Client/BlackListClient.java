package manitasUnidas.solicitud.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Usamos el nombre que Eureka muestra en tu panel: MS-BLACKLIST
@FeignClient(name = "ms-blacklist") 
public interface BlackListClient {
    
    // Esta ruta debe ser EXACTA a la del Controller de Blacklist
    @GetMapping("/api/blacklist/verificar/{rut}")
    boolean estaBloqueado(@PathVariable("rut") String rut);
}