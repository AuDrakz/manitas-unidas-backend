package manitasUnidas.fichaVet.service;

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
        return fichaRepo.findById(id).orElse(null);
    }

    /**
     * ELIMINAR: Borra el registro médico por ID.
     */
    public void deleteFicha(Long id) {
        fichaRepo.deleteById(id);
    }

    /**
     * HISTORIAL POR MASCOTA: Este es el método que te da el respaldo 
     * de cada animal usando el método que creaste en el Repository.
     */
    public List<Ficha> findByMascota(Integer idMascota) {
        return fichaRepo.findByIdMascota(idMascota);
    }

    /**
     * BUSCAR POR RUT: Para saber qué fichas hizo un veterinario específico.
     */
    public List<Ficha> findByRutVeterinario(String rut) {
        return fichaRepo.findByRut(rut);
    }

    /**
     * EDITAR: Actualiza los datos de la ficha.
     */
    public void editFicha(Ficha ficha) {
        this.saveFicha(ficha);
    }
}