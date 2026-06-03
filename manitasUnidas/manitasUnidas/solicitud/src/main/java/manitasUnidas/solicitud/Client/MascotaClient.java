package manitasUnidas.solicitud.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-mascotas")
public interface MascotaClient {

    // Obtiene el estado actual de la mascota (Disponible, Adoptado, En tratamiento)
    @GetMapping("/api/mascotas/estado/{id}")
    String obtenerEstado(@PathVariable("id") Long id);
}