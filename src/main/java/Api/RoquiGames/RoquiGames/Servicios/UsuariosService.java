package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.PerfilUsuarioDTO;
import Api.RoquiGames.RoquiGames.DTO.RegistrarUsuarioDTO;
import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import Api.RoquiGames.RoquiGames.Repositorios.UsuariosRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    private final CifradoService cifradoService;
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuariosService(CifradoService cifradoService, UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.cifradoService = cifradoService;
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PerfilUsuarioDTO obtenerPerfil(Usuarios usuarios){
        return PerfilUsuarioDTO.builder()
                .id_usuario(usuarios.getId_usuario())
                .nombre(cifradoService.descifrar(usuarios.getNombre()))
                .apellidos(cifradoService.descifrar(usuarios.getApellidos()))
                .email(cifradoService.descifrar(usuarios.getEmail()))
                .nick(usuarios.getNick())
                .build();
    }

    public PerfilUsuarioDTO registrar(RegistrarUsuarioDTO registrarUsuarioDTO){
        String mailHash=cifradoService.generarHashEmail(registrarUsuarioDTO.getEmail());
        if (usuariosRepository.findByEmailHash(mailHash).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuariosRepository.findByNick(registrarUsuarioDTO.getNick()).isPresent()) {
            throw new RuntimeException("Ya hay un usuario con esos datos");
        }

        Usuarios usuarios= new Usuarios();
                usuarios.setNombre(cifradoService.cifrar(registrarUsuarioDTO.getNombre()));
                usuarios.setApellidos(cifradoService.cifrar(registrarUsuarioDTO.getApellidos()));
                usuarios.setEmail(cifradoService.cifrar(cifradoService.normalizarEmail(registrarUsuarioDTO.getEmail())));
                usuarios.setEmailHash(mailHash);
                usuarios.setNick(registrarUsuarioDTO.getNick());
                usuarios.setPassword(passwordEncoder.encode(registrarUsuarioDTO.getPassword()));
                usuariosRepository.save(usuarios);
                return obtenerPerfil(usuarios);
    }
}
