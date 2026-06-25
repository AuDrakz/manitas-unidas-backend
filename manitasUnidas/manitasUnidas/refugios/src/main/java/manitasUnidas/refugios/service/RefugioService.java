package manitasUnidas.refugios.service;

import manitasUnidas.refugios.dto.RefugioRequestDTO;
import manitasUnidas.refugios.dto.RefugioResponseDTO;
import manitasUnidas.refugios.exception.ResourceNotFoundException;
import manitasUnidas.refugios.model.Refugio;
import manitasUnidas.refugios.repository.RefugioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio relacionada con los refugios.
 *
 * Gestiona operaciones CRUD, consultas de disponibilidad
 * y transformación de entidades hacia DTOs.
 *
 * @author Manitas Unidas
 * @version 1.0
 */
@Service
public class RefugioService {

    // Logger manual explícito para el servicio
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RefugioService.class);

    @Autowired
    private RefugioRepository refugioRepo;

    /**
     * Registra un nuevo refugio en el sistema.
     *
     * @param dto datos recibidos desde la petición
     * @return refugio creado en formato DTO
     */
    public RefugioResponseDTO guardarRefugio(RefugioRequestDTO dto) {

        log.info("Guardando refugio: {}", dto.getNombre());

        Refugio entity = new Refugio();

        entity.setNombre(dto.getNombre());
        entity.setDireccion(dto.getDireccion());
        entity.setTelefono(dto.getTelefono());
        entity.setCapacidadTotal(dto.getCapacidadTotal());
        entity.setCapacidadActual(dto.getCapacidadActual());
        entity.setResponsable(dto.getResponsable());

        Refugio saved = refugioRepo.save(entity);

        log.info("Refugio creado exitosamente con ID: {}", saved.getId());

        return mapToDTO(saved);
    }

    /**
     * Obtiene todos los refugios registrados.
     *
     * @return lista de refugios
     */
    public List<RefugioResponseDTO> listarTodos() {

        log.info("Listando todos los refugios");

        return refugioRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un refugio por su identificador.
     *
     * @param id identificador del refugio
     * @return refugio encontrado
     * @throws ResourceNotFoundException si no existe
     */
    public RefugioResponseDTO buscarPorId(Long id) {

        log.info("Buscando refugio ID: {}", id);

        Refugio ref = refugioRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refugio no existe ID: " + id));

        return mapToDTO(ref);
    }

    /**
     * Busca un refugio por nombre.
     *
     * @param nombre nombre del refugio
     * @return refugio encontrado
     * @throws ResourceNotFoundException si no existe
     */
    public RefugioResponseDTO buscarPorNombre(String nombre) {

        log.info("Buscando refugio nombre: {}", nombre);

        Refugio ref = refugioRepo.findByNombre(nombre)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refugio no existe: " + nombre));

        return mapToDTO(ref);
    }

    /**
     * Elimina un refugio existente.
     *
     * @param id identificador del refugio
     * @throws ResourceNotFoundException si no existe
     */
    public void eliminarRefugio(Long id) {

        log.info("Eliminando refugio ID: {}", id);

        Refugio ref = refugioRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refugio no existe ID: " + id));

        refugioRepo.delete(ref);

        log.info("Refugio eliminado correctamente. ID: {}", id);
    }

    /**
     * Actualiza la información de un refugio.
     *
     * @param id identificador del refugio
     * @param dto nuevos datos del refugio
     * @return refugio actualizado
     * @throws ResourceNotFoundException si no existe
     */
    public RefugioResponseDTO actualizarRefugio(
            Long id,
            RefugioRequestDTO dto) {

        log.info("Actualizando refugio ID: {}", id);

        Refugio ref = refugioRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refugio no existe ID: " + id));

        ref.setNombre(dto.getNombre());
        ref.setDireccion(dto.getDireccion());
        ref.setTelefono(dto.getTelefono());
        ref.setCapacidadTotal(dto.getCapacidadTotal());
        ref.setCapacidadActual(dto.getCapacidadActual());
        ref.setResponsable(dto.getResponsable());

        Refugio updated = refugioRepo.save(ref);

        log.info("Refugio actualizado correctamente. ID: {}", id);

        return mapToDTO(updated);
    }

    /**
     * Obtiene los refugios que aún tienen capacidad disponible.
     *
     * @return lista de refugios con cupos disponibles
     */
    public List<RefugioResponseDTO> buscarConDisponibilidad() {

        log.info("Buscando refugios con disponibilidad");

        return refugioRepo.findAll()
                .stream()
                .filter(r -> r.getCapacidadActual() < r.getCapacidadTotal())
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculates available spots for a shelter.
     *
     * @param id identificador del refugio
     * @return cantidad de cupos disponibles
     * @throws ResourceNotFoundException si no existe
     */
    public Integer obtenerCuposDisponibles(Long id) {

        log.info("Consultando cupos disponibles refugio ID: {}", id);

        Refugio ref = refugioRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refugio no existe ID: " + id));

        return ref.getCapacidadTotal() - ref.getCapacidadActual();
    }

    /**
     * Convierte una entidad Refugio a RefugioResponseDTO.
     *
     * @param entity entidad obtenida desde la base de datos
     * @return DTO de respuesta
     */
    private RefugioResponseDTO mapToDTO(Refugio entity) {

        RefugioResponseDTO dto = new RefugioResponseDTO();

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDireccion(entity.getDireccion());
        dto.setTelefono(entity.getTelefono());
        dto.setCapacidadTotal(entity.getCapacidadTotal());
        dto.setCapacidadActual(entity.getCapacidadActual());
        dto.setResponsable(entity.getResponsable());

        dto.setCuposDisponibles(
                entity.getCapacidadTotal() - entity.getCapacidadActual());

        return dto;
    }
}