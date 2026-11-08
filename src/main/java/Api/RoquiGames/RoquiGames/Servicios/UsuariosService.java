package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.PerfilUsuarioDTO;
import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    private final CifradoService cifradoService;

    public UsuariosService(CifradoService cifradoService) {
        this.cifradoService = cifradoService;
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
}
