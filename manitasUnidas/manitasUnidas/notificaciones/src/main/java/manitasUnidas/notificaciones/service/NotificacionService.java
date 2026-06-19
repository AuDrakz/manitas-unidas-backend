package manitasUnidas.notificaciones.service;

import manitasUnidas.notificaciones.model.Notificacion;
import manitasUnidas.notificaciones.repository.NotificacionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    public Notificacion procesarYEnviar(Notificacion noti) {
        log.info("[NotificacionService] Procesando notificacion para correo={}, estado={}",
                noti.getCorreoUsuario(), noti.getEstadoSolicitud());

        String mensajeFinal;
        if ("APROBADA".equalsIgnoreCase(noti.getEstadoSolicitud())) {
            mensajeFinal = construirMensajeAprobado(noti);
        } else {
            mensajeFinal = construirMensajeRechazado(noti);
        }

        noti.setMensaje(mensajeFinal);

        // Reemplazamos System.out.println por log.info
        log.info("[NotificacionService] ENVIANDO EMAIL A: {}", noti.getCorreoUsuario());
        log.info("[NotificacionService] ASUNTO: Actualizacion de tu solicitud - Manitas Unidas");
        log.info("[NotificacionService] CUERPO: {}", mensajeFinal);

        Notificacion guardada = repository.save(noti);
        log.info("[NotificacionService] Notificacion guardada con ID={}", guardada.getId());
        return guardada;
    }

    private String construirMensajeAprobado(Notificacion noti) {
        log.info("[NotificacionService] Construyendo mensaje APROBADO para mascota={}", noti.getNombreMascota());
        return "Felicidades! consideramos tu solicitud la mas apropiada para llevarte a casa a nuestr@ querid@ "
                + noti.getNombreMascota() + ", te indicamos todos los datos necesario que tienes que tener cuidado "
                + "con el nuevo integrante de la familia:\n\n"
                + "FICHA MEDICA:\n" + noti.getFichaMedicaDetalle() + "\n\n"
                + "MOTIVO DE ACEPTACION: " + noti.getMotivoAdmin() + "\n"
                + "FECHA DE RETIRO: " + noti.getFechaRetiro() + "\n"
                + "TELEFONO DE COORDINACION: " + noti.getTelefonoResponsable() + "\n\n"
                + "esperamos que le des mucho amor y carino y sea tan bien cuidado como merece.";
    }

    private String construirMensajeRechazado(Notificacion noti) {
        log.info("[NotificacionService] Construyendo mensaje RECHAZADO para mascota={}", noti.getNombreMascota());
        String base = "Lo sentimos mucho, con nuestro equipo estimamos que no cuentas con las necesidad que necesita "
                + "nuestr@ querid@ " + noti.getNombreMascota() + ", te motivamos a mejorar y volver a intentarlo. "
                + "Motivo: " + noti.getMotivoAdmin() + "\n\n";

        boolean hayMasMascotas = verificarMascotasDisponibles();

        if (hayMasMascotas) {
            base += "tenemos muchas mas mascotas esperando por un dueno que realmente los pueda amar por como son, "
                + "de seguro uno de ellos te esta esperando. puedes aplicar nuevamente a una solicitud para otra "
                + "mascota de nuestro refugio.\n"
                + "LISTADO DISPONIBLE:\n" + obtenerListaMascotasSimulada();
        } else {
            base += "Contamos con mas refugios con mascotas que estan esperando a un dueno como tu, "
                + "te invitamos a revisar en mas detalle:\n"
                + obtenerListaSedesSimulada();
        }

        return base;
    }

    // --- METODOS AUXILIARES (conectarán con Feign en siguiente iteración) ---

    private boolean verificarMascotasDisponibles() {
        // TODO: conectar con ms-mascotas via Feign
        return true;
    }

    private String obtenerListaMascotasSimulada() {
        // TODO: obtener de ms-mascotas
        return "- Toby (2 anios, Labrador, Vacunas al dia)\n- Luna (1 anio, Gato Mestizo, Sana)";
    }

    private String obtenerListaSedesSimulada() {
        // TODO: obtener de ms-refugios
        return "- Sede Norte: Av. Principal 123 | contacto@norte.cl | Responsable: Juan Perez";
    }

    public List<Notificacion> obtenerTodas() {
        log.info("[NotificacionService] Consultando historial de notificaciones");
        return repository.findAll();
    }
}