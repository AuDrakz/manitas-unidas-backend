package manitasUnidas.refugios.dto;

import jakarta.validation.constraints.*;

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

    // Constructor vacío
    public RefugioRequestDTO() {
    }

    // Constructor completo
    public RefugioRequestDTO(String nombre, String direccion, String telefono, Integer capacidadTotal, Integer capacidadActual, String responsable) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.capacidadTotal = capacidadTotal;
        this.capacidadActual = capacidadActual;
        this.responsable = responsable;
    }

    // Getters y Setters Nativos
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