package manitasUnidas.seguimiento.Repository;

import manitasUnidas.seguimiento.Model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    // HISTORIAL CLINICO COMPLETO DE LA FICHA VETERINARIA
    List<Seguimiento> findByFichaVetId(Long fichaVetId);


    // BUSCAR POR ESTADO
    List<Seguimiento> findByEstado(String estado);


    // VALIDAR EXISTENCIA
    boolean existsByFichaVetId(Long fichaVetId);

}