package manitasUnidas.solicitud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitudRequestDTO {

    @NotNull(message = "El ID del adoptante es obligatorio")
    private Long idAdoptante;

    @NotBlank(message = "El RUT es necesario para verificar antecedentes")
    private String rutAdoptante;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;

    private String observaciones;

    // Constructor vacío
    public SolicitudRequestDTO() {
    }

    // Constructor completo
    public SolicitudRequestDTO(Long idAdoptante, String rutAdoptante, Long idMascota, String observaciones) {
        this.idAdoptante = idAdoptante;
        this.rutAdoptante = rutAdoptante;
        this.idMascota = idMascota;
        this.observaciones = observaciones;
    }

    // Getters y Setters Nativos
    public Long getIdAdoptante() {
        return idAdoptante;
    }

    public void setIdAdoptante(Long idAdoptante) {
        this.idAdoptante = idAdoptante;
    }

    public String getRutAdoptante() {
        return rutAdoptante;
    }

    public void setRutAdoptante(String rutAdoptante) {
        this.rutAdoptante = rutAdoptante;
    }

    public Long getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Long idMascota) {
        this.idMascota = idMascota;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}