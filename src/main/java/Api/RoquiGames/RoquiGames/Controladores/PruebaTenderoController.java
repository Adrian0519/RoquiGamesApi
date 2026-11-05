package Api.RoquiGames.RoquiGames.Controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaTenderoController {

    @GetMapping("api/tienda/prueba")
    public String pruebaTendero() {

        return "Tendero identificado";

    }
}
