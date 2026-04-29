package manitasUnidas.mascotas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.repository.MascotaRepository;

@Service
public class MascotaService {
    @Autowired
    private MascotaRepository mascotaRepository;

    public List<Mascota> obtenerTodas() {
        return mascotaRepository.findAll();
    }

    public Mascota registrarMascota(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con el id: " + id));
    }

    public void eliminar (Long id) {
        mascotaRepository.deleteById(id);
    }
}
