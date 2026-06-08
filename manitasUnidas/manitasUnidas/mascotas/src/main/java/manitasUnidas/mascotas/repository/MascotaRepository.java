package manitasUnidas.mascotas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manitasUnidas.mascotas.model.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByEspecieIgnoreCase(String especie);

    boolean existsById(Long id);
}