package manitasUnidas.notificaciones.service;

import manitasUnidas.notificaciones.model.Notificacion;
import manitasUnidas.notificaciones.repository.NotificacionRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    public Notificacion procesarYEnviar(Notificacion noti) {
        String mensajeFinal = "";

        if ("APROBADA".equalsIgnoreCase(noti.getEstadoSolicitud())) {
            mensajeFinal = construirMensajeAprobado(noti);
        } else {
            mensajeFinal = construirMensajeRechazado(noti);
        }

        noti.setMensaje(mensajeFinal); // Guardamos el mensaje generado
        
        // Aquí iría la lógica de JavaMailSender para enviar el correo real
        System.out.println("ENVIANDO EMAIL A: " + noti.getCorreoUsuario());
        System.out.println("ASUNTO: Actualización de tu solicitud - Manitas Unidas");
        System.out.println("CUERPO: " + mensajeFinal);

        return repository.save(noti);
    }

    private String construirMensajeAprobado(Notificacion noti) {
        return "Felicidades! consideramos tu solicitud la más apropiada para llevarte a casa a nuestr@ querid@ " 
                + noti.getNombreMascota() + ", te indicamos todos los datos necesario que tienes que tener cuidado "
                + "con el nuevo integrante de la familia:\n\n"
                + "FICHA MÉDICA:\n" + noti.getFichaMedicaDetalle() + "\n\n"
                + "MOTIVO DE ACEPTACIÓN: " + noti.getMotivoAdmin() + "\n"
                + "FECHA DE RETIRO: " + noti.getFechaRetiro() + "\n"
                + "TELÉFONO DE COORDINACIÓN: " + noti.getTelefonoResponsable() + "\n\n"
                + "esperamos que le des mucho amor y cariño y sea tan bien cuidado como merece.";
    }

    private String construirMensajeRechazado(Notificacion noti) {
        String base = "Lo sentimos mucho, con nuestro equipo estimamos que no cuentas con las necesidad que necesita "
                + "nuestr@ querid@ " + noti.getNombreMascota() + ", te motivamos a mejorar y volver a intentarlo. "
                + "Motivo: " + noti.getMotivoAdmin() + "\n\n";

        // Lógica condicional para las mascotas o sedes
        boolean hayMasMascotas = verificarMascotasDisponibles(); 

        if (hayMasMascotas) {
            base += "tenemos muchas más mascotas esperando por un dueño que realmente los pueda amar por como son, "
                + "de seguro uno de ellos te esta esperando. puedes aplicar nuevamente a una solicitud para otra "
                + "mascota de nuestro refugio.\n"
                + "LISTADO DISPONIBLE:\n" + obtenerListaMascotasSimulada();
        } else {
            base += "Contamos con más refugios con mascotas que estan esperando a un dueño como tu, "
                + "te invitamos a revisar en más detalle:\n"
                + obtenerListaSedesSimulada();
        }
        
        return base;
    }

    // --- MÉTODOS AUXILIARES (Que luego conectarán con Feign) ---

    private boolean verificarMascotasDisponibles() {
        // Aquí llamarás a ms-mascotas
        return true; 
    }

    private String obtenerListaMascotasSimulada() {
        // Esto vendrá de ms-mascotas
        return "- Toby (2 años, Labrador, Vacunas al día)\n- Luna (1 año, Gato Mestizo, Sana)";
    }

    private String obtenerListaSedesSimulada() {
        // Esto vendrá de ms-refugios
        return "- Sede Norte: Av. Principal 123 | contacto@norte.cl | Responsable: Juan Pérez";
    }

        // Agrega esto al final de NotificacionService.java
    public List<Notificacion> obtenerTodas() {
        return repository.findAll();
    }
}