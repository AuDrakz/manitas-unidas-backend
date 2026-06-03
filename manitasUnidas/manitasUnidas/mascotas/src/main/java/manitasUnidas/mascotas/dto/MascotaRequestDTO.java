package manitasUnidas.mascotas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para crear o actualizar una mascota.
 * Separa el contrato de la API de la entidad JPA interna.
 */
@Data
public class MascotaRequestDTO {

    @NotBlank(message = "ERROR: El nombre de la mascota es obligatorio")
    private String nombre;

    @NotBlank(message = "ERROR: Debe especificar la especie (Perro, Gato, etc.)")
    private String especie;

    @NotBlank(message = "ERROR: Debe especificar la raza")
    private String raza;

    @NotNull(message = "ERROR: La edad es obligatoria")
    @Min(value = 0, message = "ERROR: La edad no puede ser negativa")
    private Integer edad;

    private String sexo;

    private String descripcion;

    @NotBlank(message = "ERROR: El estado es obligatorio (Disponible, Adoptado, En tratamiento)")
    private String estado;

    @NotNull(message = "ERROR: El ID del refugio es obligatorio")
    private Long refugioId;

    @NotNull(message = "ERROR: El ID del dueño es obligatorio")
    private Long duenoId;
}