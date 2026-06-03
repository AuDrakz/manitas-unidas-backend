package manitasUnidas.solicitud.Repository;

import manitasUnidas.solicitud.Model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Regla de negocio: un adoptante no puede tener más de una solicitud PENDIENTE
    boolean existsByIdAdoptanteAndEstado(Long idAdoptante, String estado);
}