package manitasUnidas.seguimiento.Service;

import manitasUnidas.seguimiento.Model.Seguimiento;
import manitasUnidas.seguimiento.Repository.SeguimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeguimientoService {
    @Autowired
    private SeguimientoRepository repository;

    public Seguimiento crearSeguimiento(Seguimiento seguimiento) {
        return repository.save(seguimiento);
    }

    public List<Seguimiento> obtenerHistorialPorSolicitud(Long solicitudId) {
        return repository.findBySolicitudId(solicitudId);
    }

}
