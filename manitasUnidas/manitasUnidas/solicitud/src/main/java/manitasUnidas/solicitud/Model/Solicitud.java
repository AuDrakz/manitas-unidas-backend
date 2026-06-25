package manitasUnidas.solicitud.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idAdoptante;
    private String rutAdoptante;
    private Long idMascota;
    private LocalDate fechaSolicitud;
    private String estado; // PENDIENTE, APROBADO, RECHAZADO
    private String observaciones;

    // Constructor vacío obligatorio para JPA
    public Solicitud() {
    }

    // Constructor completo
    public Solicitud(Long id, Long idAdoptante, String rutAdoptante, Long idMascota, LocalDate fechaSolicitud, String estado, String observaciones) {
        this.id = id;
        this.idAdoptante = idAdoptante;
        this.rutAdoptante = rutAdoptante;
        this.idMascota = idMascota;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y Setters Nativos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}