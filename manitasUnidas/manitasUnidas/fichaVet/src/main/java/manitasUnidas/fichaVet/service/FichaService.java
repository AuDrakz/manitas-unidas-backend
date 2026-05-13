package manitasUnidas.fichaVet.service;

import manitasUnidas.fichaVet.exception.ResourceNotFoundException;
import manitasUnidas.fichaVet.model.Ficha;
import manitasUnidas.fichaVet.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepo;

    /**
     * Guarda una nueva ficha o actualiza una existente.
     * Aquí se aplican las validaciones que definiste en el Model al persistir.
     */
    public void saveFicha(Ficha ficha) {
        fichaRepo.save(ficha);
    }

    /**
     * Retorna la lista completa de todas las fichas del albergue.
     */
    public List<Ficha> getFichas() {
        return fichaRepo.findAll();
    }

    /**
     * Busca una ficha médica por su ID único.
     */
    public Ficha findFicha(Long id) {
        return fichaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la ficha médica con ID: " + id));
    }

    /**
     * ELIMINAR: Borra el registro médico por ID.
     */
    public void deleteFicha(Long id) {
        Ficha ficha = this.findFicha(id); // Si no existe, lanza la excepción aquí
        fichaRepo.delete(ficha);
    }

    /**
     * HISTORIAL POR MASCOTA: Este es el método que te da el respaldo 
     * de cada animal usando el método que creaste en el Repository.
     */
    public List<Ficha> findByMascota(Integer idMascota) {
        List<Ficha> fichas = fichaRepo.findByIdMascota(idMascota);
        if (fichas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron registros médicos para la mascota con ID: " + idMascota);
        }
        return fichas;
    }

    /**
     * BUSCAR POR RUT: Para saber qué fichas hizo un veterinario específico.
     */
    public List<Ficha> findByRutVeterinario(String rut) {
        List<Ficha> fichas = fichaRepo.findByRut(rut);
        if (fichas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron fichas registradas por el veterinario RUT: " + rut);
        }
        return fichas;
    }

    public Ficha editFicha(Long id, Ficha fichaActualizada) {

    Ficha fichaExistente = fichaRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Ficha no encontrada"));

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