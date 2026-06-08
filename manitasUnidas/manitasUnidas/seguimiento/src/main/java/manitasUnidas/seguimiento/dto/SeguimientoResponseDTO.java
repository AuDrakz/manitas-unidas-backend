package manitasUnidas.seguimiento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para responder información de seguimientos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguimientoResponseDTO {

    // =========================
    // IDENTIFICACIÓN
    // =========================

    private Long id;

    // =========================
    // FICHA VETERINARIA
    // =========================

    private Long fichaVetId;

    // =========================
    // ESTADO DEL SEGUIMIENTO
    // =========================

    private String estado;

    // =========================
    // COMENTARIO
    // =========================

    private String comentario;

    // =========================
    // HISTORIAL DE ACTUALIZACIONES
    // =========================

    private List<LocalDate> fechasActualizacion;
}