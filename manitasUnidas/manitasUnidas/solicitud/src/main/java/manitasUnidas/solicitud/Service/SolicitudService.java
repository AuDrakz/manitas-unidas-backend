package manitasUnidas.solicitud.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Repository.SolicitudRepository;
import manitasUnidas.solicitud.Client.BlackListClient;
import manitasUnidas.solicitud.Client.MascotaClient; // Importamos el cliente de mascotas
import manitasUnidas.solicitud.DTO.SolicitudRequestDTO;
import manitasUnidas.solicitud.Exception.SolicitudRechazadaException;
import manitasUnidas.solicitud.Exception.ResourceNotFoundException; // Asegúrate de tener esta excepción

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    @Autowired
    private BlackListClient blackListClient;

    @Autowired
    private MascotaClient mascotaClient; // Inyectamos el cliente de mascotas

    // 1. Crear con doble validación (Blacklist y Existencia de Mascota)
    public Solicitud crearSolicitud(SolicitudRequestDTO dto) {
        
        // --- VALIDACIÓN 1: LISTA NEGRA ---
        boolean bloqueado = blackListClient.estaBloqueado(dto.getRutAdoptante());
        if (bloqueado) {
            throw new SolicitudRechazadaException("El adoptante con RUT " + dto.getRutAdoptante() + " está en la lista negra.");
        }

        // --- VALIDACIÓN 2: EXISTENCIA DE MASCOTA ---
        try {
            // Llamamos al microservicio de mascotas
            Object mascota = mascotaClient.obtenerMascota(dto.getIdMascota());
            if (mascota == null) {
                throw new ResourceNotFoundException("La mascota con ID " + dto.getIdMascota() + " no existe.");
            }
        } catch (Exception e) {
            // Si el micro de mascotas responde error o no está disponible
            throw new ResourceNotFoundException("No se puede proceder: La mascota con ID " + dto.getIdMascota() + " no fue encontrada.");
        }

        // --- PROCESO DE GUARDADO ---
        Solicitud solicitud = new Solicitud();
        solicitud.setIdAdoptante(dto.getIdAdoptante());
        solicitud.setRutAdoptante(dto.getRutAdoptante());
        solicitud.setIdMascota(dto.getIdMascota());
        solicitud.setObservaciones(dto.getObservaciones());
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDate.now());

        return repository.save(solicitud);
    }

    // 2. Obtener todas
    public List<Solicitud> obtenerTodas() {
        return repository.findAll();
    }

    // 3. Buscar por ID
    public Solicitud buscarPorId(Long id) {
        Optional<Solicitud> op = repository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    // 4. Cambiar Estado (Update)
    public Solicitud cambiarEstado(Long id, String nuevoEstado) {
        Optional<Solicitud> op = repository.findById(id);
        if (op.isPresent()) {
            Solicitud solicitud = op.get();
            solicitud.setEstado(nuevoEstado);
            return repository.save(solicitud);
        }
        return null;
    }

    // 5. Eliminar (Delete)
    public boolean eliminarSolicitud(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}