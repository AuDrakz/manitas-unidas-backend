package manitasUnidas.refugios.dto;

public class RefugioResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private Integer capacidadTotal;
    private Integer capacidadActual;
    private Integer cuposDisponibles;
    private String responsable;

    // Constructor vacío
    public RefugioResponseDTO() {
    }

    // Constructor completo
    public RefugioResponseDTO(Long id, String nombre, String direccion, String telefono, Integer capacidadTotal, Integer capacidadActual, Integer cuposDisponibles, String responsable) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.capacidadTotal = capacidadTotal;
        this.capacidadActual = capacidadActual;
        this.cuposDisponibles = cuposDisponibles;
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

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}