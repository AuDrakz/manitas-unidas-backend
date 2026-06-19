package manitasUnidas.blackList.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import manitasUnidas.blackList.exception.ResourceNotFoundException;
import manitasUnidas.blackList.model.BlackList;
import manitasUnidas.blackList.repository.BlackListRepository;

@Slf4j
@Service
public class BlackListService {

    @Autowired
    private BlackListRepository repository;

    public List<BlackList> obtenerTodos() {
        log.info("[BlackListService] Consultando todos los registros de lista negra");
        return repository.findAll();
    }

    public BlackList bloquearUsuario(BlackList registro) {
        log.info("[BlackListService] Intentando bloquear RUT={}", registro.getRut());
        if (repository.existsByRut(registro.getRut())) {
            log.warn("[BlackListService] RUT={} ya esta en lista negra", registro.getRut());
            throw new RuntimeException("El RUT " + registro.getRut() + " ya esta en la lista negra.");
        }
        registro.setFechaBloqueo(LocalDate.now());
        BlackList guardado = repository.save(registro);
        log.info("[BlackListService] RUT={} bloqueado con ID={}", registro.getRut(), guardado.getId());
        return guardado;
    }

    public BlackList buscarPorRut(String rut) {
        log.info("[BlackListService] Verificando si RUT={} esta en lista negra", rut);
        return repository.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("[BlackListService] RUT={} no esta sancionado", rut);
                    return new ResourceNotFoundException("El RUT " + rut + " no esta sancionado.");
                });
    }

    public BlackList obtenerPorId(Long id) {
        log.info("[BlackListService] Buscando registro ID={}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[BlackListService] ID={} no existe en lista negra", id);
                    return new ResourceNotFoundException("No existe un registro en la lista negra con el ID: " + id);
                });
    }

    public void desbloquear(Long id) {
        log.info("[BlackListService] Desbloqueando registro ID={}", id);
        if (!repository.existsById(id)) {
            log.warn("[BlackListService] No se puede desbloquear: ID={} no existe", id);
            throw new ResourceNotFoundException("No se puede eliminar: No existe el ID: " + id);
        }
        repository.deleteById(id);
        log.info("[BlackListService] Registro ID={} eliminado de lista negra", id);
    }

    public BlackList actualizarBlackList(Long id, BlackList datosNuevos) {
        log.info("[BlackListService] Actualizando registro ID={}", id);
        BlackList existente = obtenerPorId(id);
        existente.setRut(datosNuevos.getRut());
        existente.setAutorizadoPor(datosNuevos.getAutorizadoPor());
        existente.setMotivo(datosNuevos.getMotivo());
        existente.setFechaBloqueo(datosNuevos.getFechaBloqueo());
        BlackList actualizado = repository.save(existente);
        log.info("[BlackListService] Registro ID={} actualizado", id);
        return actualizado;
    }
}