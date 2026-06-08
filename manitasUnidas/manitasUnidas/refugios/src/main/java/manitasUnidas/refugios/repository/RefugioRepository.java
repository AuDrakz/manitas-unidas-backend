package manitasUnidas.refugios.repository;

import manitasUnidas.refugios.model.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefugioRepository extends JpaRepository<Refugio, Long> {

    Optional<Refugio> findByNombre(String nombre);

    List<Refugio> findByDireccion(String direccion);
}