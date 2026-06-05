package manitasUnidas.seguimiento.Service;

import manitasUnidas.seguimiento.Model.Seguimiento;
import manitasUnidas.seguimiento.Repository.SeguimientoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguimientoService {

    private static final Logger logger =
            LoggerFactory.getLogger(SeguimientoService.class);

    private final SeguimientoRepository repository;

    public SeguimientoService(SeguimientoRepository repository) {
        this.repository = repository;
    }

    // CREAR SEGUIMIENTO
    public Seguimiento crearSeguimiento(Seguimiento seguimiento) {

        logger.info("Creando nuevo seguimiento para fichaVetId={}", seguimiento.getFichaVetId());

        Seguimiento saved = repository.save(seguimiento);

        logger.info("Seguimiento creado con ID={}", saved.getId());

        return saved;
    }

    // LISTAR TODOS LOS SEGUIMIENTOS
    public List<Seguimiento> listarSeguimientos() {

        logger.info("Listando todos los seguimientos");

        return repository.findAll();
    }

    // BUSCAR POR ID
    public Seguimiento obtenerPorId(Long id) {

        logger.info("Buscando seguimiento con ID={}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Seguimiento no encontrado con ID={}", id);
                    return new RuntimeException("Seguimiento no encontrado");
                });
    }

    // HISTORIAL CLINICO
    public List<Seguimiento> obtenerHistorialClinico(Long fichaVetId) {

        logger.info("Consultando historial clínico para fichaVetId={}", fichaVetId);

        return repository.findByFichaVetIdOrderByIdDesc(fichaVetId);
    }

    // BUSCAR POR ESTADO
    public List<Seguimiento> buscarPorEstado(String estado) {

        logger.info("Buscando seguimientos por estado={}", estado);

        return repository.findByEstadoOrderByIdDesc(estado);
    }

    // VALIDAR EXISTENCIA
    public boolean existeFichaVet(Long fichaVetId) {

        logger.info("Validando existencia de fichaVetId={}", fichaVetId);

        return repository.existsByFichaVetId(fichaVetId);
    }

    // ACTUALIZAR
    public Seguimiento actualizarSeguimiento(Long id, Seguimiento nuevo) {

        logger.info("Actualizando seguimiento ID={}", id);

        Seguimiento existente = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("No existe seguimiento con ID={}", id);
                    return new RuntimeException("Seguimiento no encontrado");
                });

        existente.setFichaVetId(nuevo.getFichaVetId());
        existente.setEstado(nuevo.getEstado());
        existente.setComentario(nuevo.getComentario());

        Seguimiento updated = repository.save(existente);

        logger.info("Seguimiento actualizado ID={}", updated.getId());

        return updated;
    }

    // ELIMINAR
    public void eliminarSeguimiento(Long id) {

        logger.warn("Eliminando seguimiento ID={}", id);

        repository.deleteById(id);
    }
}