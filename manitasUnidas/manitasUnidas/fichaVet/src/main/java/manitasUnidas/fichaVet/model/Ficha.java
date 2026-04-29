package manitasUnidas.fichaVet.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name= "Ficha")
@AllArgsConstructor
@NoArgsConstructor
public class Ficha {

    //DATOS GENERALES 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @PastOrPresent
    @NotNull(message = "Debe tener una fecha agendada")
    private LocalDateTime fechaConsulta;

//========================  VETERINARIO =========================
    //IDENTIFICAR
    @NotBlank (message = "Es obligatorio ingresar un rut.")
    private String rut;
    //NOMBRE 
    @NotBlank(message = "Debe tener un veterinario")
    private String veterinario;
//=========================  MASCOTA    ==========================
    //DATOS DE LA MASCOTA
    @NotNull (message = "La mascota debe tener un id identificativo")
    private Integer idMascota;

    //MOTIVO 
    @NotBlank(message = "Debe indicar el motivo de la consulta")
    @Size(min = 10,max = 255)
    private String motivoConsulta;

    //PESO
    @Positive(message = "El peso no puede ser negativo, Ingrese un dato valido (max 200KG)")
    @Max(200)
    private Double peso;

    //TEMPERATURA
    @Range(min = 30,max = 45,message = "¡T° Imposible!. ingresa un valor valido dentro del rango (30-45°C)")
    private Double temperatura;

    //FRECUENCIA
    @Positive(message = "No puede tener frecuencia negativa")
    private Integer frecuenciaCardiaca;

    //DIAGNOSTICO
    @NotBlank(message = "Obligatoriamente debe indicar diagnostico, de lo contrario especificar que se evaluó")
    @Size(min =  5,message = "Especifique y expresese de manera clara")
    private String diagnostico;

    //TRATAMIENTO
    @NotBlank(message = "Debe tener indicaciones veterinarias")
    private String tratamiento;

    //OBSERVACIONES
    @Size(max = 1000,message = "Indicar las observaciones de manera clara es Obligatorio")
    private String Observaciones;

    //=========================     HISTORIAL MASCOTA    ==========================

    //CASTRADO
    @NotNull(message = "Obligatorio indicar su estado")
    private boolean castrado;

    //ENFERMEDADES/CONDICIONES/ALERGIAS
    @Size(max = 500,message = "Es Obligatorio indicar enfermedades y/o condiociones de salud de la mascota\nEnfermedades Cronicas\nAlergias\nCondiciones Medicas")
    private String enfermedadesCondicion;

    //TIPO DE SANGRE
    @Pattern(regexp = "^(DEA [1-8](\\.[1-9])?|Desconocido)$", message = "Formato de sangre inválido")
    private String tipoSangre;

    //VACUNAS
    @NotBlank(message = "El esquema de vacunación no puede estar vacío")
    @Size(min = 5, max = 1000, message = "Debe detallar las vacunas aplicadas")
    private String esquemaVacunacion;

    //FECHA DE DESPARACITACION
    @NotNull(message = "La fecha de desparasitación es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser mayor al día de hoy")
    private LocalDate ultimaDesparasitacion;







    




}
