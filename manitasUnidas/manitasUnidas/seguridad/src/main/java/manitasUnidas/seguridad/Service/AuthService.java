package manitasUnidas.seguridad.service;

import manitasUnidas.seguridad.model.AuthRequest;
import manitasUnidas.seguridad.model.Usuario;
import manitasUnidas.seguridad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Guarda el usuario encriptando la contraseña con BCrypt
    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRol() == null) {
            usuario.setRol("USER");
        }
        return usuarioRepository.save(usuario);
    }

    // Valida las credenciales contra la base de datos MySQL
    public String login(AuthRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Compara el texto plano con el hash de la BD
            if (passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
                return jwtService.generateToken(usuario.getUsername());
            }
        }
        return "Usuario o contraseña incorrectos";
    }
}