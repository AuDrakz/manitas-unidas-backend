package manitasUnidas.blackList.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manitasUnidas.blackList.Model.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long>{
    
    // Para verificar rápidamente si existe el RUT
    Optional<BlackList> findByRut(String rut);
    
    boolean existsByRut(String rut);
    
} 