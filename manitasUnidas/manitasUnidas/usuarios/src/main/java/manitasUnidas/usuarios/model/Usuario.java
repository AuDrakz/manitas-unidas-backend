package manitasUnidas.usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Error: rut no puede ser vacio")
    private String rut;

    @NotBlank(message = "Error: nombre no puede ser vacio")
    private String nombre;

    @Email
    @NotBlank(message = "Error: correo no puede ser vacio")
    private String correo;

    @NotBlank(message = "Error: contraseña no puede ser vacio")
    private String password;

    private String telefono;

    private String direccion;

    private String rol;
}
