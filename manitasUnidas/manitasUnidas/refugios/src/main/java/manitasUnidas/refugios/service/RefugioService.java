package manitasUnidas.refugios.service;

import manitasUnidas.refugios.exception.ResourceNotFoundException;
import manitasUnidas.refugios.model.Refugio;
import manitasUnidas.refugios.repository.RefugioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefugioService {

    @Autowired
    private RefugioRepository refugioRepo;

    // GUARDAR: Ahora retorna el objeto guardado
    public Refugio guardarRefugio(Refugio refugio) {
        return refugioRepo.save(refugio);
    }

    // LISTAR
    public List<Refugio> listarTodos() {
        return refugioRepo.findAll();
    }

    // BUSCAR POR ID
    public Refugio buscarPorId(Long id) {
        return refugioRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El refugio con ID " + id + " no existe."));
    }

    // BUSCAR POR NOMBRE
    public Refugio buscarPorNombre(String nombre) {
        Refugio ref = refugioRepo.findByNombre(nombre);
        if (ref == null) {
            throw new ResourceNotFoundException("No se encontró el refugio: " + nombre);
        }
        return ref;
    }

    // ELIMINAR
    public void eliminarRefugio(Long id) {
        // Primero verificamos si existe para lanzar la excepción si no
        Refugio ref = this.buscarPorId(id);
        refugioRepo.delete(ref);
    }

    // BUSCAR REFUGIOS CON DISPONIBILIDAD
    public List<Refugio> buscarConDisponibilidad() {
        return refugioRepo.findAll().stream()
                .filter(r -> r.getCapacidadActual() < r.getCapacidadTotal())
                .collect(Collectors.toList());
    }

    // CÁLCULO DE CUPOS
    public Integer obtenerCuposDisponibles(Long id) {
        Refugio ref = this.buscarPorId(id);
        return ref.getCapacidadTotal() - ref.getCapacidadActual();
    }

    // ACTUALIZAR: También retorna el objeto actualizado
    public Refugio actualizarRefugio(Refugio refugio) {
        // Verificamos que exista antes de actualizar
        this.buscarPorId(refugio.getId());
        return refugioRepo.save(refugio);
    }
}