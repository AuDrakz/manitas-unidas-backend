package manitasUnidas.mascotas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una mascota registrada en el sistema.
 * Contiene la información básica de identificación, estado y relaciones
 * con refugios y propietarios.
 */
@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    /**
     * Identificador único de la mascota.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la mascota.
     */
    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Especie de la mascota.
     * Ejemplo: Perro, Gato, Conejo.
     */
    @NotBlank(message = "Debe especificar la especie")
    @Size(max = 50, message = "La especie no puede superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String especie;

    /**
     * Raza de la mascota.
     */
    @NotBlank(message = "Debe especificar la raza")
    @Size(max = 100, message = "La raza no puede superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String raza;

    /**
     * Edad de la mascota.
     */
    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Column(nullable = false)
    private Integer edad;

    /**
     * Sexo de la mascota.
     */
    @Size(max = 20, message = "El sexo no puede superar los 20 caracteres")
    @Column(length = 20)
    private String sexo;

    /**
     * Descripción adicional.
     */
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    /**
     * Estado actual de la mascota.
     * Ejemplo: Disponible, Adoptado, En tratamiento.
     */
    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no puede superar los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String estado;

    /**
     * Identificador del refugio asociado.
     */
    @NotNull(message = "El ID del refugio es obligatorio")
    @Column(nullable = false)
    private Long refugioId;

    /**
     * Identificador del propietario o responsable.
     */
    @NotNull(message = "El ID del dueño es obligatorio")
    @Column(nullable = false)
    private Long duenoId;
}