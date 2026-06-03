package manitasUnidas.mascotas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import manitasUnidas.mascotas.client.RefugioClient;
import manitasUnidas.mascotas.client.UsuarioClient;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.exception.ResourceNotFoundException;
import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.repository.MascotaRepository;

@Slf4j   
@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private RefugioClient refugioClient;

    public List<Mascota> obtenerTodas() {
        log.info("[MascotaService] Consultando todas las mascotas");
        return mascotaRepository.findAll();
    }

    public Mascota registrarMascota(MascotaRequestDTO dto) {
        log.info("[MascotaService] Intentando registrar mascota: nombre={}, duenoId={}, refugioId={}",
                dto.getNombre(), dto.getDuenoId(), dto.getRefugioId());

        if (!usuarioClient.verificarExistencia(dto.getDuenoId())) {
            log.warn("[MascotaService] Registro rechazado: usuario con ID {} no existe", dto.getDuenoId());
            throw new ResourceNotFoundException("Error: El usuario con ID " + dto.getDuenoId() + " no existe.");
        }

        if (!refugioClient.verificarExistencia(dto.getRefugioId())) {
            log.warn("[MascotaService] Registro rechazado: refugio con ID {} no existe", dto.getRefugioId());
            throw new ResourceNotFoundException("Error: El refugio con ID " + dto.getRefugioId() + " no existe.");
        }

        Mascota mascota = new Mascota();
        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setEdad(dto.getEdad());
        mascota.setSexo(dto.getSexo());
        mascota.setDescripcion(dto.getDescripcion());
        mascota.setEstado(dto.getEstado());
        mascota.setRefugioId(dto.getRefugioId());
        mascota.setDuenoId(dto.getDuenoId());

        Mascota guardada = mascotaRepository.save(mascota);
        log.info("[MascotaService] Mascota registrada exitosamente con ID={}", guardada.getId());
        return guardada;
    }

    public Mascota buscarPorId(Long id) {
        log.info("[MascotaService] Buscando mascota con ID={}", id);
        return mascotaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[MascotaService] Mascota con ID={} no encontrada", id);
                    return new ResourceNotFoundException("Mascota no encontrada con el id: " + id);
                });
    }

    public Mascota actualizarMascota(Long id, MascotaRequestDTO dto) {
        log.info("[MascotaService] Intentando actualizar mascota ID={}", id);

        Mascota mascotaExistente = buscarPorId(id);

        if (!usuarioClient.verificarExistencia(dto.getDuenoId())) {
            log.warn("[MascotaService] Actualización rechazada: nuevo dueño con ID {} no existe", dto.getDuenoId());
            throw new ResourceNotFoundException("No se puede actualizar: El nuevo dueño con ID " + dto.getDuenoId() + " no existe.");
        }

        if (!refugioClient.verificarExistencia(dto.getRefugioId())) {
            log.warn("[MascotaService] Actualización rechazada: refugio con ID {} no existe", dto.getRefugioId());
            throw new ResourceNotFoundException("No se puede actualizar: El refugio con ID " + dto.getRefugioId() + " no existe.");
        }

        mascotaExistente.setNombre(dto.getNombre());
        mascotaExistente.setEspecie(dto.getEspecie());
        mascotaExistente.setRaza(dto.getRaza());
        mascotaExistente.setEdad(dto.getEdad());
        mascotaExistente.setSexo(dto.getSexo());
        mascotaExistente.setDescripcion(dto.getDescripcion());
        mascotaExistente.setEstado(dto.getEstado());
        mascotaExistente.setRefugioId(dto.getRefugioId());
        mascotaExistente.setDuenoId(dto.getDuenoId());

        Mascota actualizada = mascotaRepository.save(mascotaExistente);
        log.info("[MascotaService] Mascota ID={} actualizada exitosamente", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("[MascotaService] Eliminando mascota con ID={}", id);
        buscarPorId(id); // Lanza 404 si no existe antes de eliminar
        mascotaRepository.deleteById(id);
        log.info("[MascotaService] Mascota ID={} eliminada", id);
    }
}