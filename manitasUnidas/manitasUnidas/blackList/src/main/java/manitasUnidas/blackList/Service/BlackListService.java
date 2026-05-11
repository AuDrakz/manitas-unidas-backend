package manitasUnidas.blackList.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import manitasUnidas.blackList.Model.BlackList;
import manitasUnidas.blackList.Repository.BlackListRepository;
import manitasUnidas.blackList.exception.ResourceNotFoundException;
@Service
public class BlackListService {
    @Autowired
    private BlackListRepository repository;

    public List<BlackList> obtenerTodos() {
        return repository.findAll();
    }

    public BlackList bloquearUsuario(BlackList registro) {
    if (repository.existsByRut(registro.getRut())) {
        // El Handler lo capturará y enviará un 400
        throw new RuntimeException("El RUT " + registro.getRut() + " ya está en la lista negra.");
    }
    registro.setFechaBloqueo(LocalDate.now());
    return repository.save(registro);
    }

    public BlackList buscarPorRut(String rut) {
    return repository.findByRut(rut)
            .orElseThrow(() -> new ResourceNotFoundException("El RUT " + rut + " no está sancionado."));
    }

    public BlackList obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un registro en la lista negra con el ID: " + id));
    }

    public void desbloquear(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: No existe el ID: + " + id);
        }
        repository.deleteById(id);
    }

    public BlackList actualizarBlackList(Long id, BlackList datosNuevos) {
        BlackList blackListExistente = obtenerPorId(id);

        blackListExistente.setRut(datosNuevos.getRut());
        blackListExistente.setAutorizadoPor(datosNuevos.getAutorizadoPor());
        blackListExistente.setMotivo(datosNuevos.getMotivo());
        blackListExistente.setFechaBloqueo(datosNuevos.getFechaBloqueo());

        return repository.save(blackListExistente);
    }

}
