package manitasUnidas.fichaVet.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FichaResponseDTO {

    private Long idFicha;
    private LocalDateTime fechaConsulta;

    private String rut;
    private String veterinario;

    private Long idMascota;

    private String motivoConsulta;

    private Double peso;
    private Double temperatura;
    private Integer frecuenciaCardiaca;

    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    private Boolean castrado;
    private String enfermedadesCondicion;
    private String tipoSangre;
    private String esquemaVacunacion;

    private LocalDate ultimaDesparasitacion;
}