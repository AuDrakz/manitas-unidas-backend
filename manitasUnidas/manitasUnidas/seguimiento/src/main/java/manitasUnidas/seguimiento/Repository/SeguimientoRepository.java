package manitasUnidas.seguimiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manitasUnidas.seguimiento.model.Seguimiento;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    // HISTORIAL COMPLETO DE UNA FICHA VETERINARIA
    List<Seguimiento> findByFichaVetIdOrderByIdDesc(Long fichaVetId);

    // FILTRAR POR ESTADO
    List<Seguimiento> findByEstadoOrderByIdDesc(String estado);

    // VALIDAR SI EXISTE SEGUIMIENTO PARA UNA FICHA
    boolean existsByFichaVetId(Long fichaVetId);
}