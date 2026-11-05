package Api.RoquiGames.RoquiGames.Controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaAdminController {

    @GetMapping("api/admin/prueba")
    public String pruebaAdmin() {

        return "Credenciales de admin aceptadas";

    }
}
