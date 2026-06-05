package manitasUnidas.notificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor  // <-- AGREGADO OBLIGATORIO
@AllArgsConstructor // <-- AGREGADO OBLIGATORIO
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- DATOS DEL DESTINATARIO ---
    private String correoUsuario;
    private String nombreUsuario;

    // --- ESTADO Y MOTIVO (ADMIN/VET) ---
    private String estadoSolicitud; // APROBADA o RECHAZADA

    @Column(length = 1000)
    private String motivoAdmin; 

    // --- DATOS DE LA MASCOTA ---
    private String nombreMascota;

    // --- DATOS DE LA FICHA MÉDICA ---
    @Column(columnDefinition = "TEXT")
    private String fichaMedicaDetalle; 

    // --- DATOS DE RETIRO ---
    private LocalDateTime fechaRetiro;
    private String telefonoResponsable;

    // --- FECHA DE ENVÍO ---
    private LocalDateTime fechaEnvio;

    // --- EL CAMPO QUE FALTABA DENTRO ---
    @Column(columnDefinition = "TEXT")
    private String mensaje; 

    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = LocalDateTime.now();
    }
}