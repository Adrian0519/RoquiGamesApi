package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.CrearVideojuegoDTO;
import Api.RoquiGames.RoquiGames.DTO.MostrarVideojuegoDTO;
import Api.RoquiGames.RoquiGames.Servicios.VideojuegosService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tienda/videojuegos")
public class GestionVideojuegosController {

    private final VideojuegosService videojuegosService;

    public GestionVideojuegosController(VideojuegosService videojuegosService) {
        this.videojuegosService = videojuegosService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MostrarVideojuegoDTO crearVideojuego(@RequestBody CrearVideojuegoDTO crearVideojuegoDTO) {
        return videojuegosService.crearVideojuego(crearVideojuegoDTO);
    }

}
