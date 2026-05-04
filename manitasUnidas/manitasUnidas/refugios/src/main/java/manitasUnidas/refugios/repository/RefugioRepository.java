package manitasUnidas.refugios.repository;

import manitasUnidas.refugios.model.Refugio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefugioRepository extends JpaRepository<Refugio, Long> {


    // Busca refugios por comuna.
    // Aunque la dirección es un String único, si el usuario escribe la comuna,
    // este método ayudará a encontrar coincidencias.

    List<Refugio> findByDireccion(String comuna);


    //Busca refugios que tengan capacidad disponible.
    // Compara que la ocupación actual sea menor al límite enviado.
    List<Refugio> findByOcupacionActual(Integer limite);


     //Busca refugios por nombre de forma exacta.   
    Refugio findByNombre(String nombre);
}