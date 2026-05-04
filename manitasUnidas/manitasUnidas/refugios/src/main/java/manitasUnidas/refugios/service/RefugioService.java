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

    // GUARDAR
    public void guardarRefugio(Refugio refugio) {
        refugioRepo.save(refugio);
    }

    // LISTAR
    public List<Refugio> listarTodos() {
        return refugioRepo.findAll();
    }

    // BUSCAR POR ID
    public Refugio buscarPorId(Long id) {
    return refugioRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("El refugio con ID " + id + " no existe en el sistema."));
}

    // BUSCAR POR RESPONSABLE (Lógica de filtrado)
    public List<Refugio> buscarPorResponsable(String responsable) {
        return refugioRepo.findAll().stream()
                .filter(r -> r.getResponsable() != null && r.getResponsable().equalsIgnoreCase(responsable))
                .collect(Collectors.toList());
    }

    // BUSCAR POR COMUNA / DIRECCIÓN EXACTA
    public List<Refugio> buscarPorDireccion(String direccion) {
        return refugioRepo.findByDireccion(direccion);
    }

    // BUSCAR POR NOMBRE
    public Refugio buscarPorNombre(String nombre) {
    Refugio ref = refugioRepo.findByNombre(nombre);
    if (ref == null) {
        throw new ResourceNotFoundException("No se encontró ningún refugio con el nombre: " + nombre);
    }
    return ref;
}

    // ELIMINAR
    public void eliminarRefugio(Long id) {
        refugioRepo.deleteById(id);
    }

    // BUSCAR REFUGIOS QUE NO ESTÉN AL MÁXIMO
    public List<Refugio> buscarConDisponibilidad() {
        return refugioRepo.findAll().stream()
                .filter(r -> r.getCapacidadActual() < r.getCapacidadTotal())
                .collect(Collectors.toList());
    }

    // INDICAR CANTIDAD DE CUPOS DISPONIBLES (Cálculo dinámico)
    public Integer obtenerCuposDisponibles(Long id) {
    // Al usar buscarPorId, si no existe, ya lanzará la excepción automáticamente
    Refugio ref = this.buscarPorId(id);
    return ref.getCapacidadTotal() - ref.getCapacidadActual();
}

    // ACTUALIZAR / EDITAR
    public void actualizarRefugio(Refugio refugio) {
        this.guardarRefugio(refugio);
    }
}