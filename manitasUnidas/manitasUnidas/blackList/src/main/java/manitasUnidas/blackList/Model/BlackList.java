package manitasUnidas.blackList.Model;

import java.time.LocalDate;

import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "lista_negra")
@Data
public class BlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Column(unique = true) // No queremos duplicados en la lista negra
    private String rut;

    @NotBlank(message = "Debe especificar el motivo del bloqueo")
    private String motivo;

    private LocalDate fechaBloqueo;

    @NotBlank(message = "Debe registrar quién autoriza el bloqueo")
    private String autorizadoPor;


}
