package manitasUnidas.solicitud.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "solicitudes")
@Data
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idAdoptante;
    private String rutAdoptante; 
    private Long idMascota;      
    private LocalDate fechaSolicitud;
    private String estado; // PENDIENTE, APROBADO, RECHAZADO
    private String observaciones;

}
