package manitasUnidas.fichaVet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ficha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ficha {

    // =========================
    // IDENTIFICADOR
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha")
    private Long idFicha;

    // =========================
    // DATOS GENERALES
    // =========================
    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;

    // =========================
    // VETERINARIO
    // =========================
    @Column(name = "rut_veterinario")
    private String rut;

    @Column(name = "nombre_veterinario")
    private String veterinario;

    // =========================
    // MASCOTA
    // =========================
    @Column(name = "id_mascota")
    private Long idMascota;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    // =========================
    // SIGNOS VITALES
    // =========================
    @Column(name = "peso")
    private Double peso;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    // =========================
    // DIAGNÓSTICO Y TRATAMIENTO
    // =========================
    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "tratamiento")
    private String tratamiento;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    // =========================
    // HISTORIAL MASCOTA
    // =========================
    @Column(name = "castrado")
    private Boolean castrado;

    @Column(name = "enfermedades_condicion", length = 500)
    private String enfermedadesCondicion;

    @Column(name = "tipo_sangre")
    private String tipoSangre;

    @Column(name = "esquema_vacunacion", length = 1000)
    private String esquemaVacunacion;

    @Column(name = "ultima_desparasitacion")
    private LocalDate ultimaDesparasitacion;
}