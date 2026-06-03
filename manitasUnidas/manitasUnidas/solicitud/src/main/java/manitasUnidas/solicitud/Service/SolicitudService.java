package manitasUnidas.solicitud.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Repository.SolicitudRepository;
import manitasUnidas.solicitud.Client.BlackListClient;
import manitasUnidas.solicitud.Client.MascotaClient;
import manitasUnidas.solicitud.Client.UsuarioClient;   // <-- NUEVO
import manitasUnidas.solicitud.DTO.SolicitudRequestDTO;
import manitasUnidas.solicitud.Exception.SolicitudRechazadaException;
import manitasUnidas.solicitud.Exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Slf4j   // <-- AGREGA ESTE
@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    @Autowired
    private BlackListClient blackListClient;

    @Autowired
    private MascotaClient mascotaClient;

    @Autowired
    private UsuarioClient usuarioClient;   // <-- NUEVO: valida que el adoptante exista

    // 1. Crear solicitud con validaciones completas
    public Solicitud crearSolicitud(SolicitudRequestDTO dto) {
        log.info("[SolicitudService] Creando solicitud: adoptante ID={}, rut={}, mascota ID={}",
                dto.getIdAdoptante(), dto.getRutAdoptante(), dto.getIdMascota());

        // VALIDACIÓN 1: El adoptante debe existir en ms-usuarios
        if (!usuarioClient.existeUsuario(dto.getIdAdoptante())) {
            log.warn("[SolicitudService] Adoptante con ID={} no existe en ms-usuarios", dto.getIdAdoptante());
            throw new ResourceNotFoundException("El adoptante con ID " + dto.getIdAdoptante() + " no existe en el sistema.");
        }

        // VALIDACIÓN 2: Lista negra
        boolean bloqueado = blackListClient.estaBloqueado(dto.getRutAdoptante());
        if (bloqueado) {
            log.warn("[SolicitudService] Adoptante RUT={} bloqueado en lista negra", dto.getRutAdoptante());
            throw new SolicitudRechazadaException("El adoptante con RUT " + dto.getRutAdoptante() + " está en la lista negra.");
        }

        // VALIDACIÓN 3: La mascota debe existir y estar disponible
        String estadoMascota;
        try {
            estadoMascota = mascotaClient.obtenerEstado(dto.getIdMascota());
        } catch (Exception e) {
            log.error("[SolicitudService] Error al consultar mascota ID={}: {}", dto.getIdMascota(), e.getMessage());
            throw new ResourceNotFoundException("No se puede proceder: La mascota con ID " + dto.getIdMascota() + " no fue encontrada.");
        }

        if (!"Disponible".equalsIgnoreCase(estadoMascota)) {
            log.warn("[SolicitudService] Mascota ID={} no disponible, estado actual={}", dto.getIdMascota(), estadoMascota);
            throw new SolicitudRechazadaException("La mascota con ID " + dto.getIdMascota() + " no está disponible (estado: " + estadoMascota + ").");
        }

        // VALIDACIÓN 4: Un adoptante no puede tener más de una solicitud PENDIENTE
        boolean tienePendiente = repository.existsByIdAdoptanteAndEstado(dto.getIdAdoptante(), "PENDIENTE");
        if (tienePendiente) {
            log.warn("[SolicitudService] Adoptante ID={} ya tiene una solicitud PENDIENTE", dto.getIdAdoptante());
            throw new SolicitudRechazadaException("El adoptante ya tiene una solicitud en estado PENDIENTE. Debe esperar resolución.");
        }

        // Guardado
        Solicitud solicitud = new Solicitud();
        solicitud.setIdAdoptante(dto.getIdAdoptante());
        solicitud.setRutAdoptante(dto.getRutAdoptante());
        solicitud.setIdMascota(dto.getIdMascota());
        solicitud.setObservaciones(dto.getObservaciones());
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDate.now());

        Solicitud guardada = repository.save(solicitud);
        log.info("[SolicitudService] Solicitud creada exitosamente con ID={}", guardada.getId());
        return guardada;
    }

    // 2. Listar todas
    public List<Solicitud> obtenerTodas() {
        log.info("[SolicitudService] Listando todas las solicitudes");
        return repository.findAll();
    }

    // 3. Buscar por ID -- CAMBIO: ahora lanza excepción en vez de retornar null
    public Solicitud buscarPorId(Long id) {
        log.info("[SolicitudService] Buscando solicitud ID={}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[SolicitudService] Solicitud ID={} no encontrada", id);
                    return new ResourceNotFoundException("Solicitud no encontrada con ID: " + id);
                });
    }

    // 4. Cambiar estado -- CAMBIO: ahora lanza excepción en vez de retornar null
    public Solicitud cambiarEstado(Long id, String nuevoEstado) {
        log.info("[SolicitudService] Cambiando estado de solicitud ID={} a '{}'", id, nuevoEstado);
        Solicitud solicitud = buscarPorId(id);
        solicitud.setEstado(nuevoEstado);
        Solicitud actualizada = repository.save(solicitud);
        log.info("[SolicitudService] Estado de solicitud ID={} actualizado a '{}'", id, nuevoEstado);
        return actualizada;
    }

    // 5. Eliminar -- CAMBIO: lanza excepción en vez de retornar boolean
    public void eliminarSolicitud(Long id) {
        log.info("[SolicitudService] Eliminando solicitud ID={}", id);
        buscarPorId(id); // Lanza 404 si no existe
        repository.deleteById(id);
        log.info("[SolicitudService] Solicitud ID={} eliminada", id);
    }
}