package manitasUnidas.blackList.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import manitasUnidas.blackList.Model.BlackList;
import manitasUnidas.blackList.Repository.BlackListRepository;
@Service
public class BlackListService {
    @Autowired
    private BlackListRepository repository;

    public List<BlackList> obtenerTodos() {
        return repository.findAll();
    }

    public BlackList bloquearUsuario(BlackList registro) {
        if (repository.existsByRut(registro.getRut())) {
            throw new RuntimeException("Este RUT ya se encuentra en la lista negra.");
        }
        registro.setFechaBloqueo(LocalDate.now());
        return repository.save(registro);
    }

    public BlackList buscarPorRut(String rut) {
        return repository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("El RUT no está en la lista negra."));
    }

    public void desbloquear(Long id) {
        repository.deleteById(id);
    }

}
