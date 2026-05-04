package manitasUnidas.solicitud.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import manitasUnidas.solicitud.Model.Solicitud;
import manitasUnidas.solicitud.Repository.SolicitudRepository;
import manitasUnidas.solicitud.Client.BlackListClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository repository;

    @Autowired
    private BlackListClient blackListClient; // Usamos el cliente para preguntar a la BlackList

    public Solicitud crearSolicitud(Solicitud solicitud) {
        // 1. Verificamos si el RUT está bloqueado en el otro microservicio
        boolean bloqueado = blackListClient.estaBloqueado(solicitud.getRutAdoptante());
        
        if (bloqueado) {
            throw new RuntimeException("No se puede crear la solicitud: El RUT está en la lista negra.");
        }

        // 2. Si no está bloqueado, llenamos los datos y guardamos
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setEstado("PENDIENTE");
        return repository.save(solicitud);
    }

    public List<Solicitud> obtenerTodas() {
        return repository.findAll();
    }

}
