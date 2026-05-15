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


    // CREAR SEGUIMIENTO
    public Seguimiento crearSeguimiento(Seguimiento seguimiento) {
        return repository.save(seguimiento);
    }


    // LISTAR TODOS LOS SEGUIMIENTOS
    public List<Seguimiento> listarSeguimientos() {
        return repository.findAll();
    }


    // BUSCAR SEGUIMIENTO POR ID
    public Seguimiento obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }


    // OBTENER HISTORIAL CLINICO COMPLETO DE LA FICHA VETERINARIA
    public List<Seguimiento> obtenerHistorialClinico(Long fichaVetId) {
        return repository.findByFichaVetId(fichaVetId);
    }


    // BUSCAR POR ESTADO
    public List<Seguimiento> buscarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }


    // VALIDAR SI EXISTE LA FICHA VETERINARIA
    public boolean existeFichaVet(Long fichaVetId) {
        return repository.existsByFichaVetId(fichaVetId);
    }


    // ACTUALIZAR SEGUIMIENTO
    public Seguimiento actualizarSeguimiento(Long id, Seguimiento nuevoSeguimiento) {

        Seguimiento seguimientoExistente = repository.findById(id).orElse(null);

        if (seguimientoExistente != null) {

            seguimientoExistente.setFichaVetId(nuevoSeguimiento.getFichaVetId());
            seguimientoExistente.setEstado(nuevoSeguimiento.getEstado());
            seguimientoExistente.setComentario(nuevoSeguimiento.getComentario());

            return repository.save(seguimientoExistente);
        }

        return null;
    }


    // ELIMINAR SEGUIMIENTO
    public void eliminarSeguimiento(Long id) {
        repository.deleteById(id);
    }
}