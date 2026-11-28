package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.MostrarVideojuegoDTO;
import Api.RoquiGames.RoquiGames.Servicios.VideojuegosService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/videojuegos")
public class VideojuegosController {

    private final VideojuegosService videojuegosService;

    public VideojuegosController(VideojuegosService videojuegosService) {
        this.videojuegosService = videojuegosService;
    }

    @GetMapping("/{id}")
    public MostrarVideojuegoDTO mostrarVideojuego(@PathVariable Integer id) {
        return videojuegosService.obtenerVideojuego(id);
    }

    @GetMapping
    public List<MostrarVideojuegoDTO> listarVideojuegos(@RequestParam(required = false) String titulo,
                                                        @RequestParam(required = false) List<String> plataforma,
                                                        @RequestParam(required = false) List<String> genero,
                                                        @RequestParam(required = false) String modalidad,
                                                        @RequestParam(required = false) BigDecimal precioMin,
                                                        @RequestParam(required = false) BigDecimal precioMax) {
        return videojuegosService.listarVideojuegos(titulo, plataforma, genero, modalidad, precioMin, precioMax);
    }

}
