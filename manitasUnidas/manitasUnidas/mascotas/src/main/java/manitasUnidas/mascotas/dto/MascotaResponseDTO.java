package manitasUnidas.mascotas.dto;

import lombok.Data;

@Data
public class MascotaResponseDTO {

    private Long id;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
    private String sexo;
    private String descripcion;
    private String estado;
    private Long refugioId;
    private Long duenoId;
}