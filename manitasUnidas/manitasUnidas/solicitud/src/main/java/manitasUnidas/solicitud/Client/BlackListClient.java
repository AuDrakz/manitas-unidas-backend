package manitasUnidas.solicitud.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "black-list")// Nombre exacto que pusiste en el application.properties de BlackList
public interface BlackListClient {
    
    @GetMapping("/api/lista-negra/verificar/{rut}")
    boolean estaBloqueado(@PathVariable("rut") String rut);

}
