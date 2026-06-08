package manitasUnidas.mascotas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import manitasUnidas.mascotas.client.RefugioClient;
import manitasUnidas.mascotas.client.UsuarioClient;
import manitasUnidas.mascotas.dto.MascotaRequestDTO;
import manitasUnidas.mascotas.dto.MascotaResponseDTO;
import manitasUnidas.mascotas.exception.ResourceNotFoundException;
import manitasUnidas.mascotas.model.Mascota;
import manitasUnidas.mascotas.repository.MascotaRepository;

@Slf4j
@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final UsuarioClient usuarioClient;
    private final RefugioClient refugioClient;

    public MascotaService(MascotaRepository mascotaRepository,
                          UsuarioClient usuarioClient,
                          RefugioClient refugioClient) {
        this.mascotaRepository = mascotaRepository;
        this.usuarioClient = usuarioClient;
        this.refugioClient = refugioClient;
    }

    public List<MascotaResponseDTO> obtenerTodas() {
        log.info("Consultando todas las mascotas");
        return mascotaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public MascotaResponseDTO registrarMascota(MascotaRequestDTO dto) {

        validarRelaciones(dto.getDuenoId(), dto.getRefugioId());

        Mascota mascota = toEntity(dto);

        Mascota guardada = mascotaRepository.save(mascota);

        log.info("Mascota registrada id={}", guardada.getId());

        return toDTO(guardada);
    }

    public MascotaResponseDTO buscarPorId(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada id=" + id));

        return toDTO(mascota);
    }

    public MascotaResponseDTO actualizarMascota(Long id, MascotaRequestDTO dto) {

        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada id=" + id));

        validarRelaciones(dto.getDuenoId(), dto.getRefugioId());

        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setEdad(dto.getEdad());
        mascota.setSexo(dto.getSexo());
        mascota.setDescripcion(dto.getDescripcion());
        mascota.setEstado(dto.getEstado());
        mascota.setDuenoId(dto.getDuenoId());
        mascota.setRefugioId(dto.getRefugioId());

        return toDTO(mascotaRepository.save(mascota));
    }

    public void eliminar(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada id=" + id));

        mascotaRepository.delete(mascota);
    }

    public boolean existePorId(Long id) {
        return mascotaRepository.existsById(id);
    }

    public String obtenerEstado(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada id=" + id));

        return mascota.getEstado();
    }

    private void validarRelaciones(Long duenoId, Long refugioId) {
        if (!usuarioClient.verificarExistencia(duenoId)) {
            throw new ResourceNotFoundException("Usuario no existe id=" + duenoId);
        }

        if (!refugioClient.verificarExistencia(refugioId)) {
            throw new ResourceNotFoundException("Refugio no existe id=" + refugioId);
        }
    }

    private Mascota toEntity(MascotaRequestDTO dto) {
        Mascota m = new Mascota();
        m.setNombre(dto.getNombre());
        m.setEspecie(dto.getEspecie());
        m.setRaza(dto.getRaza());
        m.setEdad(dto.getEdad());
        m.setSexo(dto.getSexo());
        m.setDescripcion(dto.getDescripcion());
        m.setEstado(dto.getEstado());
        m.setDuenoId(dto.getDuenoId());
        m.setRefugioId(dto.getRefugioId());
        return m;
    }

    private MascotaResponseDTO toDTO(Mascota m) {
        MascotaResponseDTO dto = new MascotaResponseDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setEspecie(m.getEspecie());
        dto.setRaza(m.getRaza());
        dto.setEdad(m.getEdad());
        dto.setSexo(m.getSexo());
        dto.setDescripcion(m.getDescripcion());
        dto.setEstado(m.getEstado());
        dto.setDuenoId(m.getDuenoId());
        dto.setRefugioId(m.getRefugioId());
        return dto;
    }
}