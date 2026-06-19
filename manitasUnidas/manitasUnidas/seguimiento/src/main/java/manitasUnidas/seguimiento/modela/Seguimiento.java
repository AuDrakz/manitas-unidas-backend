package manitasUnidas.seguimiento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa el seguimiento clínico
 * asociado a una ficha veterinaria.
 */
@Entity
@Table(name = "seguimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seguimiento {

    /**
     * Identificador único del seguimiento.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador de la ficha veterinaria asociada.
     */
    @NotNull(message = "Debe existir una ficha veterinaria")
    private Long fichaVetId;

    /**
     * Estado actual del seguimiento.
     */
    @NotBlank(message = "Debe indicar obligatoriamente el estado")
    @Size(max = 100, message = "El estado no puede superar los 100 caracteres")
    private String estado;

    /**
     * Comentario o descripción de la situación.
     */
    @NotBlank(message = "Debe indicar motivo o comentario de la situación")
    @Size(
            min = 5,
            max = 500,
            message = "El comentario debe contener entre 5 y 500 caracteres"
    )
    private String comentario;

    /**
     * Historial de fechas de actualización.
     */
    @ElementCollection
    @CollectionTable(
            name = "fechas_seguimiento",
            joinColumns = @JoinColumn(name = "seguimiento_id")
    )
    @Column(name = "fecha_actualizacion")
    private List<LocalDate> fechasActualizacion = new ArrayList<>();

    /**
     * Registra automáticamente la fecha de creación.
     */
    @PrePersist
    protected void onCreate() {
        fechasActualizacion.add(LocalDate.now());
    }

    /**
     * Registra automáticamente la fecha de actualización.
     */
    @PreUpdate
    protected void onUpdate() {
        fechasActualizacion.add(LocalDate.now());
    }
}