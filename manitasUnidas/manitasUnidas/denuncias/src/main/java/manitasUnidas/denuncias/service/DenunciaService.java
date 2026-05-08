package manitasUnidas.denuncias.service;

import manitasUnidas.denuncias.client.UsuarioClient;
import manitasUnidas.denuncias.exception.ResourceNotFoundException;
import manitasUnidas.denuncias.model.Denuncia;
import manitasUnidas.denuncias.repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository repository;

    @Autowired
    private UsuarioClient usuarioClient;

    public List<Denuncia> listarTodas() {
        return repository.findAll();
    }

    public Denuncia guardar(Denuncia denuncia) {
        if (!usuarioClient.verificarExistencia(denuncia.getDenuncianteId())) {
            throw new ResourceNotFoundException("El usuario: " + denuncia.getDenuncianteId() + " no existe");
        }
        denuncia.setFechaReporte(LocalDate.now());
        denuncia.setEstado("PENDIENTE");
        return repository.save(denuncia);
    }

    public Denuncia buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La denuncia con ID " + id + " no existe."));
    }

    public Denuncia actualizarEstado(Long id, String nuevoEstado) {
        // primero buscarmos la denuncia, cambiamos estado y guardamos los cambios en la bd
        Denuncia denuncia = buscarPorId(id);
        denuncia.setEstado(nuevoEstado);
        return repository.save(denuncia);
        
    }

    public void eliminar(Long id) {
        Denuncia d = buscarPorId(id); 
        repository.delete(d);
    }
}