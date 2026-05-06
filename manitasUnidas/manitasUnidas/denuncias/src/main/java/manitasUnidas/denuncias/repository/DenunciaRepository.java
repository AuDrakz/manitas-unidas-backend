package manitasUnidas.denuncias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manitasUnidas.denuncias.model.Denuncia;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    // método extra para buscar denuncias contra alguien especifico
    List<Denuncia> findByRutDenunciado(String rutDenunciado);
}
