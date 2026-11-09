package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.MostrarVideojuegoDTO;
import Api.RoquiGames.RoquiGames.Servicios.VideojuegosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/videojuegos")
public class VideojuegosController {

    private final VideojuegosService videojuegosService;

    public VideojuegosController(VideojuegosService videojuegosService) {
        this.videojuegosService = videojuegosService;
    }

    @GetMapping
    public List<MostrarVideojuegoDTO>listarVideojuegos(){
        return videojuegosService.listarVideojuegos();
    }
}
