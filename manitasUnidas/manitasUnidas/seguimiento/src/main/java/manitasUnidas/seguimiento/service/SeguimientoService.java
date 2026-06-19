package manitasUnidas.seguimiento.service;

import lombok.extern.slf4j.Slf4j;
import manitasUnidas.seguimiento.dto.SeguimientoRequestDTO;
import manitasUnidas.seguimiento.dto.SeguimientoResponseDTO;
import manitasUnidas.seguimiento.exception.ResourceNotFoundException;
import manitasUnidas.seguimiento.model.Seguimiento;
import manitasUnidas.seguimiento.repository.SeguimientoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la gestión de seguimientos clínicos.
 *
 * Permite registrar, consultar, actualizar y eliminar
 * seguimientos asociados a fichas veterinarias.
 */
@Service
@Slf4j
public class SeguimientoService {

    @Autowired
    private SeguimientoRepository repository;

    /**
     * Registra un nuevo seguimiento.
     *
     * @param dto datos recibidos desde la petición
     * @return seguimiento creado
     */
    public SeguimientoResponseDTO crearSeguimiento(
            SeguimientoRequestDTO dto) {

        log.info(
                "Creando nuevo seguimiento para fichaVetId={}",
                dto.getFichaVetId());

        Seguimiento seguimiento = new Seguimiento();

        seguimiento.setFichaVetId(dto.getFichaVetId());
        seguimiento.setEstado(dto.getEstado());
        seguimiento.setComentario(dto.getComentario());

        Seguimiento saved = repository.save(seguimiento);

        log.info("Seguimiento creado con ID={}", saved.getId());

        return mapToDTO(saved);
    }

    /**
     * Obtiene todos los seguimientos.
     *
     * @return lista de seguimientos
     */
    public List<SeguimientoResponseDTO> listarSeguimientos() {

        log.info("Listando todos los seguimientos");

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un seguimiento por ID.
     *
     * @param id identificador
     * @return seguimiento encontrado
     */
    public SeguimientoResponseDTO obtenerPorId(Long id) {

        log.info("Buscando seguimiento con ID={}", id);

        Seguimiento seguimiento = repository.findById(id)
                .orElseThrow(() -> {
                    log.error(
                            "Seguimiento no encontrado con ID={}",
                            id);

                    return new ResourceNotFoundException(
                            "Seguimiento no encontrado con ID: " + id);
                });

        return mapToDTO(seguimiento);
    }

    /**
     * Obtiene el historial clínico asociado
     * a una ficha veterinaria.
     *
     * @param fichaVetId identificador de ficha
     * @return historial completo
     */
    public List<SeguimientoResponseDTO>
    obtenerHistorialClinico(Long fichaVetId) {

        log.info(
                "Consultando historial clínico para fichaVetId={}",
                fichaVetId);

        return repository.findByFichaVetIdOrderByIdDesc(fichaVetId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca seguimientos por estado.
     *
     * @param estado estado a consultar
     * @return lista de seguimientos
     */
    public List<SeguimientoResponseDTO>
    buscarPorEstado(String estado) {

        log.info("Buscando seguimientos por estado={}", estado);

        return repository.findByEstadoOrderByIdDesc(estado)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si existen seguimientos asociados
     * a una ficha veterinaria.
     *
     * @param fichaVetId identificador de ficha
     * @return true si existe
     */
    public boolean existeFichaVet(Long fichaVetId) {

        log.info(
                "Validando existencia de fichaVetId={}",
                fichaVetId);

        return repository.existsByFichaVetId(fichaVetId);
    }

    /**
     * Actualiza un seguimiento existente.
     *
     * @param id identificador
     * @param dto nuevos datos
     * @return seguimiento actualizado
     */
    public SeguimientoResponseDTO actualizarSeguimiento(
            Long id,
            SeguimientoRequestDTO dto) {

        log.info("Actualizando seguimiento ID={}", id);

        Seguimiento existente = repository.findById(id)
                .orElseThrow(() -> {
                    log.error(
                            "No existe seguimiento con ID={}",
                            id);

                    return new ResourceNotFoundException(
                            "Seguimiento no encontrado con ID: " + id);
                });

        existente.setFichaVetId(dto.getFichaVetId());
        existente.setEstado(dto.getEstado());
        existente.setComentario(dto.getComentario());

        Seguimiento updated = repository.save(existente);

        log.info(
                "Seguimiento actualizado correctamente ID={}",
                updated.getId());

        return mapToDTO(updated);
    }

    /**
     * Elimina un seguimiento.
     *
     * @param id identificador del seguimiento
     */
    public void eliminarSeguimiento(Long id) {

        log.warn("Eliminando seguimiento ID={}", id);

        Seguimiento seguimiento = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seguimiento no encontrado con ID: " + id));

        repository.delete(seguimiento);

        log.info("Seguimiento eliminado correctamente");
    }

    /**
     * Convierte una entidad Seguimiento
     * a SeguimientoResponseDTO.
     *
     * @param seguimiento entidad
     * @return DTO de respuesta
     */
    private SeguimientoResponseDTO mapToDTO(
            Seguimiento seguimiento) {

        SeguimientoResponseDTO dto =
                new SeguimientoResponseDTO();

        dto.setId(seguimiento.getId());
        dto.setFichaVetId(seguimiento.getFichaVetId());
        dto.setEstado(seguimiento.getEstado());
        dto.setComentario(seguimiento.getComentario());
        dto.setFechasActualizacion(
                seguimiento.getFechasActualizacion());

        return dto;
    }
}