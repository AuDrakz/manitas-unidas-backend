package manitasUnidas.fichaVet.service;

import manitasUnidas.fichaVet.exception.ResourceNotFoundException;
import manitasUnidas.fichaVet.model.Ficha;
import manitasUnidas.fichaVet.repository.FichaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FichaService {

    private final FichaRepository fichaRepo;

    public FichaService(FichaRepository fichaRepo) {
        this.fichaRepo = fichaRepo;
    }

    public void saveFicha(Ficha ficha) {
        log.info("Guardando ficha veterinaria para mascota {}", ficha.getIdMascota());
        fichaRepo.save(ficha);
    }

    public List<Ficha> getFichas() {
        log.info("Obteniendo todas las fichas veterinarias");
        return fichaRepo.findAll();
    }

    public Ficha findFicha(Long id) {
        log.info("Buscando ficha con id {}", id);

        return fichaRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró la ficha médica con ID: " + id));
    }

    public void deleteFicha(Long id) {
        log.warn("Eliminando ficha con id {}", id);

        Ficha ficha = this.findFicha(id);
        fichaRepo.delete(ficha);
    }

    public List<Ficha> findByMascota(Long idMascota) {
        log.info("Buscando historial de mascota {}", idMascota);

        List<Ficha> fichas = fichaRepo.findByIdMascota(idMascota);

        if (fichas.isEmpty()) {
            log.warn("No se encontraron fichas para mascota {}", idMascota);
            throw new ResourceNotFoundException("No se encontraron registros médicos para la mascota con ID: " + idMascota);
        }
        return fichas;
    }

    public List<Ficha> findByRutVeterinario(String rut) {
        log.info("Buscando fichas del veterinario {}", rut);

        List<Ficha> fichas = fichaRepo.findByRut(rut);

        if (fichas.isEmpty()) {
            log.warn("No se encontraron fichas para veterinario {}", rut);
            throw new ResourceNotFoundException("No se encontraron fichas registradas por el veterinario RUT: " + rut);
        }
        return fichas;
    }

    public Ficha editFicha(Long id, Ficha fichaActualizada) {

        log.info("Actualizando ficha {}", id);

        Ficha fichaExistente = fichaRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ficha no encontrada con ID: " + id));

        fichaExistente.setFechaConsulta(fichaActualizada.getFechaConsulta());
        fichaExistente.setRut(fichaActualizada.getRut());
        fichaExistente.setVeterinario(fichaActualizada.getVeterinario());
        fichaExistente.setIdMascota(fichaActualizada.getIdMascota());
        fichaExistente.setMotivoConsulta(fichaActualizada.getMotivoConsulta());
        fichaExistente.setPeso(fichaActualizada.getPeso());
        fichaExistente.setTemperatura(fichaActualizada.getTemperatura());
        fichaExistente.setFrecuenciaCardiaca(fichaActualizada.getFrecuenciaCardiaca());
        fichaExistente.setDiagnostico(fichaActualizada.getDiagnostico());
        fichaExistente.setTratamiento(fichaActualizada.getTratamiento());
        fichaExistente.setObservaciones(fichaActualizada.getObservaciones());
        fichaExistente.setCastrado(fichaActualizada.getCastrado());
        fichaExistente.setEnfermedadesCondicion(fichaActualizada.getEnfermedadesCondicion());
        fichaExistente.setTipoSangre(fichaActualizada.getTipoSangre());
        fichaExistente.setEsquemaVacunacion(fichaActualizada.getEsquemaVacunacion());
        fichaExistente.setUltimaDesparasitacion(fichaActualizada.getUltimaDesparasitacion());

        return fichaRepo.save(fichaExistente);
    }
}