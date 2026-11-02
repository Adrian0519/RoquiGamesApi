package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.LoginDTO;
import Api.RoquiGames.RoquiGames.DTO.LoginRespuestaDTO;
import Api.RoquiGames.RoquiGames.Servicios.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    public LoginController (LoginService loginService){
        this.loginService=loginService;
    }

    @PostMapping("/login")
    public LoginRespuestaDTO iniciarSesion(@RequestBody LoginDTO loginDTO){
        return loginService.iniciarSesion(loginDTO);
    }
}
