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
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "Debe indicar la dirección del refugio")
    @Column(nullable = false, length = 200)
    private String direccion;

    @NotBlank(message = "Debe asociar un número de teléfono")
    @Size(min = 9, max = 9, message = "Número inválido, debe contener 9 dígitos")
    @Column(nullable = false, length = 9)
    private String telefono;

    @NotNull(message = "Debe indicar la capacidad máxima")
    @Positive(message = "La capacidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer capacidadTotal;

    @NotNull(message = "Debe indicar cuántos animales hay actualmente")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer capacidadActual;

    @NotBlank(message = "Debe tener un responsable")
    @Column(nullable = false, length = 100)
    private String responsable;
}