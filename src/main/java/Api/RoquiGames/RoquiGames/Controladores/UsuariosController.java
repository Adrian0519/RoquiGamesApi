package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.PerfilUsuarioDTO;
import Api.RoquiGames.RoquiGames.DTO.RegistrarUsuarioDTO;
import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import Api.RoquiGames.RoquiGames.Servicios.UsuariosService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @GetMapping("/perfil")
    public PerfilUsuarioDTO obtenerPerfil(@AuthenticationPrincipal Usuarios usuarios){
        return usuariosService.obtenerPerfil(usuarios);

    }

    @PostMapping("/registro")
    public PerfilUsuarioDTO registrar(@RequestBody  RegistrarUsuarioDTO registrarUsuarioDTO){
        return usuariosService.registrar(registrarUsuarioDTO);
    }
}
