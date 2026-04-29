package manitasUnidas.mascotas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ERROR: El nombre de la mascota es obligatorio")
    private String nombre;

    @NotBlank(message = "ERROR: Debe especificiar la especie (Perro, Gato, etc.)")
    private String especie;

    @NotBlank(message = "ERROR: Debe especificar la raza")
    private String raza;

    @NotNull(message = "ERROR: La edad es obligatoria")
    @Min(value = 0, message = "ERROR: La edad no puede ser negativa")
    private Integer edad;

    private String sexo;

    private String descripcion;

    @NotBlank(message = "ERROR: el estado es obligatorio (Disponible, Adoptado, En tratamiento)")
    private String estado;

    // ID para mas adelante vincular con Usuario o Refugio
    private Long refugioId;
}
