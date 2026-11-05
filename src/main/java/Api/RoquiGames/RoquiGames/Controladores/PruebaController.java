package Api.RoquiGames.RoquiGames.Controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    @GetMapping("api/prueba/protegido")
    public String pruebaProtegida(){

        return "JWT correcto";

    }
}
