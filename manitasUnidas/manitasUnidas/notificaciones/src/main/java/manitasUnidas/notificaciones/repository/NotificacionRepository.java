package manitasUnidas.notificaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manitasUnidas.notificaciones.model.Notificacion;

    @Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    
    // Al solo escribir el nombre del método, Spring "entiende" el SQL solo
    List<Notificacion> findByCorreoUsuario(String correo);
}


