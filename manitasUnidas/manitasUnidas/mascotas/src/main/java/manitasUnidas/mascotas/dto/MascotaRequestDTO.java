package manitasUnidas.mascotas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO utilizado para registrar y actualizar mascotas.
 * Contiene únicamente los datos que la API recibe desde el cliente.
 */
@Data
public class MascotaRequestDTO {

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "Debe especificar la especie")
    @Size(max = 50, message = "La especie no puede superar los 50 caracteres")
    private String especie;

    @NotBlank(message = "Debe especificar la raza")
    @Size(max = 100, message = "La raza no puede superar los 100 caracteres")
    private String raza;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    @Size(max = 20, message = "El sexo no puede superar los 20 caracteres")
    private String sexo;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no puede superar los 50 caracteres")
    private String estado;

    @NotNull(message = "El ID del refugio es obligatorio")
    private Long refugioId;

    @NotNull(message = "El ID del dueño es obligatorio")
    private Long duenoId;
}