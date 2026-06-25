package manitasUnidas.refugios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "refugios")
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

    // Constructor vacío (Obligatorio para JPA)
    public Refugio() {
    }

    // Constructor con todos los campos
    public Refugio(Long id, String nombre, String direccion, String telefono, Integer capacidadTotal, Integer capacidadActual, String responsable) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.capacidadTotal = capacidadTotal;
        this.capacidadActual = capacidadActual;
        this.responsable = responsable;
    }

    // Getters y Setters Nativos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(Integer capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public Integer getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(Integer capacidadActual) {
        this.capacidadActual = capacidadActual;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}