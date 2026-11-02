package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.LoginDTO;
import Api.RoquiGames.RoquiGames.DTO.LoginRespuestaDTO;
import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import Api.RoquiGames.RoquiGames.Repositorios.UsuariosRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final JwtService jwtService;
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {

        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginRespuestaDTO iniciarSesion(LoginDTO loginDTO) {

        Usuarios usuario = usuariosRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        if (!usuario.getActivo()) {
            throw new RuntimeException("La cuenta esta desactivada");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contrasena incorrecta");
        }

        String token = jwtService.generarToken(usuario);

        return LoginRespuestaDTO.builder()
                .id_usuario(usuario.getId_usuario())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .token(token)
                .build();
    }
}
