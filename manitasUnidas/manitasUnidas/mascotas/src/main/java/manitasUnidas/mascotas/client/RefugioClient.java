package manitasUnidas.mascotas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-refugios")
public interface RefugioClient {
    @GetMapping("/api/refugios/existe/{id}")
    boolean verificarExistencia(@PathVariable("id") Long id);
}
