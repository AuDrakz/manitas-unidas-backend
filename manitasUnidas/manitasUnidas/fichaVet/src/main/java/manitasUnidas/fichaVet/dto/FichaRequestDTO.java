package manitasUnidas.fichaVet.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaRequestDTO {

    // =========================
    // DATOS GENERALES
    // =========================

    @PastOrPresent
    @NotNull(message = "Debe tener una fecha agendada")
    private LocalDateTime fechaConsulta;

    // =========================
    // VETERINARIO
    // =========================

    @NotBlank(message = "Es obligatorio ingresar un rut.")
    @Size(max = 20)
    private String rut;

    @NotBlank(message = "Debe tener un veterinario")
    @Size(max = 255)
    private String veterinario;

    // =========================
    // MASCOTA
    // =========================

    @NotNull(message = "La mascota debe tener un id identificativo")
    private Long idMascota;

    @NotBlank(message = "Debe indicar el motivo de la consulta")
    @Size(min = 10, max = 255)
    private String motivoConsulta;

    // =========================
    // SIGNOS VITALES
    // =========================

    @Positive(message = "El peso no puede ser negativo")
    @DecimalMax(value = "200.0", message = "Máximo permitido 200 KG")
    private Double peso;

    @DecimalMin(value = "30.0", message = "La temperatura mínima es 30°C")
    @DecimalMax(value = "45.0", message = "La temperatura máxima es 45°C")
    private Double temperatura;

    @Positive(message = "La frecuencia cardíaca debe ser positiva")
    private Integer frecuenciaCardiaca;

    // =========================
    // DIAGNÓSTICO Y TRATAMIENTO
    // =========================

    @NotBlank(message = "Debe indicar un diagnóstico")
    @Size(min = 5, max = 255)
    private String diagnostico;

    @NotBlank(message = "Debe indicar un tratamiento")
    @Size(max = 255)
    private String tratamiento;

    @NotBlank(message = "Debe indicar observaciones")
    @Size(max = 1000)
    private String observaciones;

    // =========================
    // HISTORIAL MASCOTA
    // =========================

    @NotNull(message = "Debe indicar si está castrado")
    private Boolean castrado;

    @Size(max = 500)
    private String enfermedadesCondicion;

    @Pattern(
        regexp = "^(DEA [1-8](\\.[1-9])?|Grupo [AB]|AB|Desconocido)$",
        message = "Formato de sangre inválido"
    )
    private String tipoSangre;

    @NotBlank(message = "Debe indicar el esquema de vacunación")
    @Size(min = 5, max = 1000)
    private String esquemaVacunacion;

    @NotNull(message = "La fecha de desparasitación es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate ultimaDesparasitacion;
}