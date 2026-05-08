package manitasUnidas.seguimiento.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguimientos")
@Data
public class Seguimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long solicitudId; // ID que viene del microservicio de Solicitudes
    private String estado;    // Ej: "Recibido", "En Proceso", "Completado"
    private String comentario;
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaActualizacion = LocalDateTime.now();
    }


}
