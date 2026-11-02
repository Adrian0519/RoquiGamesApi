package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.LoginDTO;
import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import Api.RoquiGames.RoquiGames.Repositorios.UsuariosRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {

        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Usuarios iniciarSesion(LoginDTO loginDTO) {

        Usuarios usuarios = usuariosRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));
        if (!usuarios.getActivo()) {
            throw new RuntimeException("La cuenta esta desactivada");
        }

        if (passwordEncoder.matches(loginDTO.getPassword(), usuarios.getPassword())) {
            throw new RuntimeException("Contrasena incorrecta");
        }

        return usuarios;
    }

}
