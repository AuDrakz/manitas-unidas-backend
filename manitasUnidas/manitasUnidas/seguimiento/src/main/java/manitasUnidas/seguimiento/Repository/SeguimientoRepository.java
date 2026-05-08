package manitasUnidas.seguimiento.Repository;

import manitasUnidas.seguimiento.Model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    List<Seguimiento> findBySolicitudId(Long solicitudId);

}
