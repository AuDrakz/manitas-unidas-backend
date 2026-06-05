package manitasUnidas.seguimiento.Repository;

import manitasUnidas.seguimiento.Model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    // HISTORIAL COMPLETO DE UNA FICHA VETERINARIA
    List<Seguimiento> findByFichaVetIdOrderByIdDesc(Long fichaVetId);

    // FILTRAR POR ESTADO
    List<Seguimiento> findByEstadoOrderByIdDesc(String estado);

    // VALIDAR SI EXISTE SEGUIMIENTO PARA UNA FICHA
    boolean existsByFichaVetId(Long fichaVetId);
}