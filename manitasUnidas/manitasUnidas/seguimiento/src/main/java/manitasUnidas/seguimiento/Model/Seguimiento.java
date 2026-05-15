package manitasUnidas.seguimiento.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seguimientos")
@Data
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // RELACION CON FICHAVET
    @NotNull(message = "Debe existir una ficha veterinaria")
    private Long fichaVetId;


    // ESTADO DEL SEGUIMIENTO
    @NotBlank(message = "Debe indicar obligatoriamente el estado")
    private String estado;


    // COMENTARIO DEL SEGUIMIENTO
    @NotBlank(message = "Debe indicar motivo/comentario de la situación")
    @Size(min = 5, max = 500, message = "El comentario debe contener entre 5 y 500 caracteres")
    private String comentario;


    // HISTORIAL DE FECHAS
    @ElementCollection
    @CollectionTable(
            name = "fechas_seguimiento",
            joinColumns = @JoinColumn(name = "seguimiento_id")
    )
    @Column(name = "fecha_actualizacion")
    private List<LocalDate> fechasActualizacion = new ArrayList<>();


    // FECHA AUTOMATICA AL CREAR
    @PrePersist
    protected void onCreate() {
        fechasActualizacion.add(LocalDate.now());
    }


    // FECHA AUTOMATICA AL ACTUALIZAR
    @PreUpdate
    protected void onUpdate() {
        fechasActualizacion.add(LocalDate.now());
    }
}