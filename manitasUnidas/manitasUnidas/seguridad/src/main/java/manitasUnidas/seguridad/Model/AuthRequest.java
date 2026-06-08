package manitasUnidas.seguridad.model; 

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}