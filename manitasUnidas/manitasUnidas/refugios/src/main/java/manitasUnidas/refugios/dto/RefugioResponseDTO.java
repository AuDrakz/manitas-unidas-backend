package manitasUnidas.refugios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefugioResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private Integer capacidadTotal;
    private Integer capacidadActual;
    private Integer cuposDisponibles;
    private String responsable;
}