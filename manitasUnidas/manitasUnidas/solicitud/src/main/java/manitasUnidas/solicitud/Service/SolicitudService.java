package manitasUnidas.solicitud.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Repository.SolicitudRepository;
import manitasUnidas.solicitud.Client.BlackListClient;
import manitasUnidas.solicitud.DTO.SolicitudRequestDTO;
import manitasUnidas.solicitud.Exception.ResourceNotFoundException;
import manitasUnidas.solicitud.Exception.SolicitudRechazadaException;

import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    @Autowired
    private BlackListClient blackListClient; 

    // MÉTODO PRINCIPAL: Usado por el Controller
    public Solicitud crearSolicitud(SolicitudRequestDTO dto) {
        // 1. Verificamos la lista negra (Feign llama al micro ms-blacklist)
        boolean bloqueado = blackListClient.estaBloqueado(dto.getRutAdoptante());
        
        if (bloqueado) {
            // Usamos la excepción de RECHAZO (403 Forbidden)
            throw new SolicitudRechazadaException("El adoptante con RUT " + dto.getRutAdoptante() + " tiene prohibido adoptar por estar en la lista negra.");
        }

        // 2. Mapeo: Pasamos los datos del DTO a la Entidad
        Solicitud solicitud = new Solicitud();
        solicitud.setRutAdoptante(dto.getRutAdoptante());
        solicitud.setIdMascota(dto.getIdMascota());
        solicitud.setObservaciones(dto.getObservaciones());
        
        // 3. Valores automáticos del sistema
        solicitud.setEstado("PENDIENTE");
        solicitud.setFechaSolicitud(LocalDate.now());

        // 4. Guardamos en la base de datos de ms-solicitud
        return repository.save(solicitud);
    }

    public List<Solicitud> obtenerTodas() {
        return repository.findAll();
    }

    // Ejemplo de cuándo usar ResourceNotFoundException
    public Solicitud obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la solicitud con ID: " + id));
    }
}