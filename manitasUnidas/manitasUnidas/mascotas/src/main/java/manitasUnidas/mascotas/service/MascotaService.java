package manitasUnidas.mascotas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import manitasUnidas.mascotas.client.RefugioClient;
import manitasUnidas.mascotas.client.UsuarioClient;
import manitasUnidas.mascotas.exception.ResourceNotFoundException;
import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.repository.MascotaRepository;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private RefugioClient refugioClient;

    public List<Mascota> obtenerTodas() {
        return mascotaRepository.findAll();
    }

    public Mascota registrarMascota(Mascota mascota) {
        if (!usuarioClient.verificarExistencia(mascota.getDuenoId())) {
            throw new ResourceNotFoundException("Error: El usuario con ID " + mascota.getDuenoId() + " no existe. ");
        }

        if (!refugioClient.verificarExistencia(mascota.getRefugioId())) {
            throw new ResourceNotFoundException("Error: El refugio con ID " + mascota.getRefugioId() + " no existe.");
        }
        return mascotaRepository.save(mascota);
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con el id: " + id));
    }

    public Mascota actualizarMascota(Long id, Mascota datosNuevos) {
        // 1. Buscamos la mascota o lanzamos el error 404 que ya tienes configurado
        Mascota mascotaExistente = buscarPorId(id);
        if (!usuarioClient.verificarExistencia(datosNuevos.getDuenoId())) {
            throw new ResourceNotFoundException("No se puede actualizar: El nuevo dueño con ID " + datosNuevos.getDuenoId() + " no existe. ");
        }
        
        if (!refugioClient.verificarExistencia(datosNuevos.getRefugioId())) {
            throw new ResourceNotFoundException("No se puede actualizar: El refugio con ID " + datosNuevos.getRefugioId() + " no existe.");
        }
        // 2. Actualizamos los campos necesarios
        mascotaExistente.setNombre(datosNuevos.getNombre());
        mascotaExistente.setEspecie(datosNuevos.getEspecie());
        mascotaExistente.setRaza(datosNuevos.getRaza());
        mascotaExistente.setEdad(datosNuevos.getEdad());
        mascotaExistente.setSexo(datosNuevos.getSexo());
        mascotaExistente.setDescripcion(datosNuevos.getDescripcion());
        mascotaExistente.setEstado(datosNuevos.getEstado());
        mascotaExistente.setRefugioId(datosNuevos.getRefugioId());
        
        // 3. Guardamos en la base de datos
        return mascotaRepository.save(mascotaExistente);
    }

    public void eliminar (Long id) {
        mascotaRepository.deleteById(id);
    }
}
