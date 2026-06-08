package manitasUnidas.solicitud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import manitasUnidas.solicitud.model.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // Regla de negocio: un adoptante no puede tener más de una solicitud PENDIENTE
    boolean existsByIdAdoptanteAndEstado(Long idAdoptante, String estado);
}