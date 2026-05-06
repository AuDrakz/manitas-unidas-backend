package manitasUnidas.denuncias.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "denuncias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario que hace la denuncia (viene del micro Usuarios)
    @Column(nullable = false)
    private Long denuncianteId;

    // El RUT o ID de la persona denunciada (para cruzar con Blacklist)
    @Column(nullable = false)
    private String rutDenunciado;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String ubicacion;

    // PENDIENTE, EN_PROCESO, RESUELTA, RECHAZADA
    private String estado;

    private String nivelGravedad;

    private LocalDate fechaReporte;

}
