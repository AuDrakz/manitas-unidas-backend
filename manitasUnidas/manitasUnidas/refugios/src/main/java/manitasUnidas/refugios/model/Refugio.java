package manitasUnidas.refugios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refugios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refugio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del refugio es obligatorio")
    private String nombre;

    @NotBlank(message = "Debe indicar la dirección del Refugio")
    private String direccion;

    @NotBlank(message = "Debe Asociar un numero de Telefono")
    @Size(min = 9, max = 9, message = "Numero invalido, complete con los 9 caracteres")
    private String telefono;

    @NotNull(message = "Debe indicar su capacidad máxima")
    @Positive(message = "La capacidad debe ser mayor a Cero")
    private Integer capacidadTotal;

    @NotNull(message = "Debe indicar cuántos animales hay actualmente")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer capacidadActual;

    @NotBlank(message = "Debe tener un Responsable del Refugio")
    private String responsable;
}

