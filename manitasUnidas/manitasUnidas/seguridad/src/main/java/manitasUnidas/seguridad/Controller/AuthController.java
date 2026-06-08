package manitasUnidas.seguridad.Controller;

import manitasUnidas.seguridad.Model.AuthRequest;
import manitasUnidas.seguridad.Model.Usuario;
import manitasUnidas.seguridad.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Módulo de Seguridad y Acceso", description = "Zona de control de usuarios. Aquí se gestiona el ingreso al sistema y el registro de nuevas cuentas.")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
        summary = "🔑 Iniciar sesión (Login)", 
        description = "Ingresa tu nombre de usuario y contraseña para entrar al sistema. Si los datos son correctos, la aplicación te entregará una clave digital de acceso (Llave/Token JWT) para usar las demás pantallas."
    )
    @ApiResponses(value = {
        // AJUSTADO: Ahora especifica que devuelve un texto plano (el Token string)
        @ApiResponse(responseCode = "200", description = "¡Bienvenido! Datos correctos. Se entrega tu llave digital de acceso.",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "401", description = "Acceso denegado: El usuario o la contraseña que escribiste no son correctos."),
        @ApiResponse(responseCode = "400", description = "Error de envío: Olvidaste llenar los campos o el formato de texto no es el correcto.")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = authService.login(authRequest);
        
        if ("Usuario o contraseña incorrectos".equals(token)) {
            return ResponseEntity.status(401).body(token);
        }
        
        return ResponseEntity.ok(token);
    }

    @Operation(
        summary = "📝 Registrar un usuario nuevo", 
        description = "Crea una cuenta nueva rellenando los datos del formulario. El sistema guardará la contraseña de forma encriptada y oculta para proteger la privacidad del usuario."
    )
    @ApiResponses(value = {
        // AJUSTADO: Muestra la estructura exacta del objeto "Usuario.class" en la respuesta exitosa
        @ApiResponse(responseCode = "200", description = "¡Felicidades! La cuenta ha sido creada con éxito y guardada en el sistema.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "No se pudo registrar: Verifica los datos enviados, o puede que el nombre de usuario ya esté ocupado por otra persona.")
    })
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(authService.registrar(usuario));
    }
}