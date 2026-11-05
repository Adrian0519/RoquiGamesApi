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
    private final CifradoService cifradoService;

    public LoginService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder, JwtService jwtService,CifradoService cifradoService) {

        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cifradoService = cifradoService;
    }

    public LoginRespuestaDTO iniciarSesion(LoginDTO loginDTO) {

        String emailHash = cifradoService.generarHashEmail(loginDTO.getEmail());
        Usuarios usuario = usuariosRepository.findByEmailHash(emailHash)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        if (!usuario.getActivo()) {
            throw new RuntimeException("La cuenta esta desactivada");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contrasena incorrecta");
        }

        String token = jwtService.generarToken(usuario);
        String nombre= cifradoService.descifrar(usuario.getNombre());
        String email= cifradoService.descifrar(usuario.getEmail());
        return LoginRespuestaDTO.builder()
                .id_usuario(usuario.getId_usuario())
                .nombre(nombre)
                .email(email)
                .rol(usuario.getRol())
                .token(token)
                .build();
    }
}
