package manitasUnidas.fichaVet.repository;

import manitasUnidas.fichaVet.model.Ficha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FichaRepository extends JpaRepository<Ficha, Long> {

    // Método personalizado para obtener el historial de una mascota por su ID

    List<Ficha> findByIdMascota(Integer idMascota);

    // Método para filtrar fichas por el RUT del veterinario
    List<Ficha> findByRut(String rut);

    // Método para buscar fichas por nombre del veterinario (opcional)
    List<Ficha> findByVeterinarioContaining(String nombre);
}