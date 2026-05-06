package manitasUnidas.seguridad.Model; // Fíjate que la M sea mayúscula

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}