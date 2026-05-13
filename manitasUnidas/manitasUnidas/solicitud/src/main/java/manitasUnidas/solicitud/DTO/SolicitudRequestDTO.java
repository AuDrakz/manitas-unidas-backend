package manitasUnidas.solicitud.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitudRequestDTO {
    @NotNull(message = "El ID del adoptante es obligatorio")
    private Long idAdoptante;

    @NotBlank(message = "El RUT es necesario para verificar antecedentes")
    private String rutAdoptante;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    private String observaciones;
}