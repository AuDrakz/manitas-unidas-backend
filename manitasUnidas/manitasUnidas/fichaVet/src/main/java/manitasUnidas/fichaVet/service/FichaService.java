package manitasUnidas.fichaVet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import manitasUnidas.fichaVet.dto.FichaRequestDTO;
import manitasUnidas.fichaVet.dto.FichaResponseDTO;
import manitasUnidas.fichaVet.exception.ResourceNotFoundException;
import manitasUnidas.fichaVet.model.Ficha;
import manitasUnidas.fichaVet.repository.FichaRepository;

@Slf4j
@Service
public class FichaService {

    private final FichaRepository fichaRepo;

    public FichaService(FichaRepository fichaRepo) {
        this.fichaRepo = fichaRepo;
    }

    // CREAR
    public FichaResponseDTO saveFicha(FichaRequestDTO dto) {

        log.info("Guardando ficha veterinaria para mascota {}", dto.getIdMascota());

        Ficha ficha = convertirAEntidad(dto);

        return convertirAResponseDTO(fichaRepo.save(ficha));
    }

    // TRAER TODAS
    public List<FichaResponseDTO> getFichas() {

        return fichaRepo.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR POR ID
    public FichaResponseDTO findFicha(Long id) {

        Ficha ficha = fichaRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la ficha médica con ID: " + id));

        return convertirAResponseDTO(ficha);
    }

    // ELIMINAR
    public void deleteFicha(Long id) {

        Ficha ficha = fichaRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la ficha médica con ID: " + id));

        fichaRepo.delete(ficha);
    }

    // HISTORIAL POR MASCOTA
    public List<FichaResponseDTO> findByMascota(Long idMascota) {

        List<Ficha> fichas = fichaRepo.findByIdMascota(idMascota);

        if (fichas.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron registros médicos para la mascota con ID: "
                            + idMascota);
        }

        return fichas.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // BUSCAR POR RUT VETERINARIO
    public List<FichaResponseDTO> findByRutVeterinario(String rut) {

        List<Ficha> fichas = fichaRepo.findByRut(rut);

        if (fichas.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron fichas registradas por el veterinario RUT: "
                            + rut);
        }

        return fichas.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // EDITAR
    public FichaResponseDTO editFicha(Long id, FichaRequestDTO dto) {

        Ficha fichaExistente = fichaRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ficha no encontrada con ID: " + id));

        fichaExistente.setFechaConsulta(dto.getFechaConsulta());
        fichaExistente.setRut(dto.getRut());
        fichaExistente.setVeterinario(dto.getVeterinario());
        fichaExistente.setIdMascota(dto.getIdMascota());
        fichaExistente.setMotivoConsulta(dto.getMotivoConsulta());
        fichaExistente.setPeso(dto.getPeso());
        fichaExistente.setTemperatura(dto.getTemperatura());
        fichaExistente.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        fichaExistente.setDiagnostico(dto.getDiagnostico());
        fichaExistente.setTratamiento(dto.getTratamiento());
        fichaExistente.setObservaciones(dto.getObservaciones());
        fichaExistente.setCastrado(dto.getCastrado());
        fichaExistente.setEnfermedadesCondicion(dto.getEnfermedadesCondicion());
        fichaExistente.setTipoSangre(dto.getTipoSangre());
        fichaExistente.setEsquemaVacunacion(dto.getEsquemaVacunacion());
        fichaExistente.setUltimaDesparasitacion(dto.getUltimaDesparasitacion());

        return convertirAResponseDTO(fichaRepo.save(fichaExistente));
    }

    // ==========================
    // MAPPERS
    // ==========================

    private Ficha convertirAEntidad(FichaRequestDTO dto) {

        Ficha ficha = new Ficha();

        ficha.setFechaConsulta(dto.getFechaConsulta());
        ficha.setRut(dto.getRut());
        ficha.setVeterinario(dto.getVeterinario());
        ficha.setIdMascota(dto.getIdMascota());
        ficha.setMotivoConsulta(dto.getMotivoConsulta());
        ficha.setPeso(dto.getPeso());
        ficha.setTemperatura(dto.getTemperatura());
        ficha.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        ficha.setDiagnostico(dto.getDiagnostico());
        ficha.setTratamiento(dto.getTratamiento());
        ficha.setObservaciones(dto.getObservaciones());
        ficha.setCastrado(dto.getCastrado());
        ficha.setEnfermedadesCondicion(dto.getEnfermedadesCondicion());
        ficha.setTipoSangre(dto.getTipoSangre());
        ficha.setEsquemaVacunacion(dto.getEsquemaVacunacion());
        ficha.setUltimaDesparasitacion(dto.getUltimaDesparasitacion());

        return ficha;
    }

    private FichaResponseDTO convertirAResponseDTO(Ficha ficha) {

        return new FichaResponseDTO(
                ficha.getIdFicha(),
                ficha.getFechaConsulta(),
                ficha.getRut(),
                ficha.getVeterinario(),
                ficha.getIdMascota(),
                ficha.getMotivoConsulta(),
                ficha.getPeso(),
                ficha.getTemperatura(),
                ficha.getFrecuenciaCardiaca(),
                ficha.getDiagnostico(),
                ficha.getTratamiento(),
                ficha.getObservaciones(),
                ficha.getCastrado(),
                ficha.getEnfermedadesCondicion(),
                ficha.getTipoSangre(),
                ficha.getEsquemaVacunacion(),
                ficha.getUltimaDesparasitacion()
        );
    }
}