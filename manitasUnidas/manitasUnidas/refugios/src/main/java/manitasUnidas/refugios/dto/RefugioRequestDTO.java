package manitasUnidas.refugios.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefugioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotNull(message = "La capacidad total es obligatoria")
    @Positive(message = "La capacidad total debe ser mayor a cero")
    private Integer capacidadTotal;

    @NotNull(message = "La capacidad actual es obligatoria")
    @Min(value = 0, message = "La capacidad actual no puede ser negativa")
    private Integer capacidadActual;

    @NotBlank(message = "El responsable es obligatorio")
    private String responsable;
}