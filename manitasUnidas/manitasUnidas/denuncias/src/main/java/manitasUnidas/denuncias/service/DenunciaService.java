package manitasUnidas.denuncias.service;

import manitasUnidas.denuncias.client.UsuarioClient;
import manitasUnidas.denuncias.exception.ResourceNotFoundException;
import manitasUnidas.denuncias.model.Denuncia;
import manitasUnidas.denuncias.repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository repository;

    @Autowired
    private UsuarioClient usuarioClient;

    public List<Denuncia> listarTodas() {
        log.info("[DenunciaService] Consultando todas las denuncias");
        return repository.findAll();
    }

    public Denuncia guardar(Denuncia denuncia) {
        log.info("[DenunciaService] Intentando registrar denuncia de denuncianteId={}", denuncia.getDenuncianteId());
        if (!usuarioClient.verificarExistencia(denuncia.getDenuncianteId())) {
            log.warn("[DenunciaService] Denunciante ID={} no existe en ms-usuarios", denuncia.getDenuncianteId());
            throw new ResourceNotFoundException("El usuario: " + denuncia.getDenuncianteId() + " no existe");
        }
        denuncia.setFechaReporte(LocalDate.now());
        denuncia.setEstado("PENDIENTE");
        Denuncia guardada = repository.save(denuncia);
        log.info("[DenunciaService] Denuncia registrada con ID={}", guardada.getId());
        return guardada;
    }

    public Denuncia buscarPorId(Long id) {
        log.info("[DenunciaService] Buscando denuncia ID={}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[DenunciaService] Denuncia ID={} no existe", id);
                    return new ResourceNotFoundException("La denuncia con ID " + id + " no existe.");
                });
    }

    public Denuncia actualizarEstado(Long id, String nuevoEstado) {
        log.info("[DenunciaService] Actualizando estado de denuncia ID={} a '{}'", id, nuevoEstado);
        Denuncia denuncia = buscarPorId(id);
        denuncia.setEstado(nuevoEstado);
        Denuncia actualizada = repository.save(denuncia);
        log.info("[DenunciaService] Estado de denuncia ID={} actualizado a '{}'", id, nuevoEstado);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("[DenunciaService] Eliminando denuncia ID={}", id);
        Denuncia d = buscarPorId(id);
        repository.delete(d);
        log.info("[DenunciaService] Denuncia ID={} eliminada", id);
    }
}