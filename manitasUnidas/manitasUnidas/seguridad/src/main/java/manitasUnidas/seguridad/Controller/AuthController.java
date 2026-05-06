package manitasUnidas.seguridad.Controller;

import manitasUnidas.seguridad.Model.AuthRequest;
import manitasUnidas.seguridad.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        // Por ahora, validación simple para que pruebes:
        if ("admin".equals(authRequest.getUsername()) && "1234".equals(authRequest.getPassword())) {
            return jwtService.generateToken(authRequest.getUsername());
        }
        return "Usuario o contraseña incorrectos";
    }

}
