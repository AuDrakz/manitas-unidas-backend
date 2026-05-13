package manitasUnidas.solicitud.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-mascotas")
public interface MascotaClient {
    @GetMapping("/api/mascotas/{id}")
    Object obtenerMascota(@PathVariable("id") Long id); 
}