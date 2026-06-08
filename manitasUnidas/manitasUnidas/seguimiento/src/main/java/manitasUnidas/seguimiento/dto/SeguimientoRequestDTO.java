package manitasUnidas.seguimiento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para crear y actualizar seguimientos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoRequestDTO {

    // =========================
    // FICHA VETERINARIA
    // =========================

    @NotNull(message = "Debe existir una ficha veterinaria")
    private Long fichaVetId;

    // =========================
    // ESTADO DEL SEGUIMIENTO
    // =========================

    @NotBlank(message = "Debe indicar obligatoriamente el estado")
    @Size(max = 100, message = "El estado no puede superar los 100 caracteres")
    private String estado;

    // =========================
    // COMENTARIO
    // =========================

    @NotBlank(message = "Debe indicar motivo o comentario")
    @Size(
        min = 5,
        max = 500,
        message = "El comentario debe contener entre 5 y 500 caracteres"
    )
    private String comentario;
}