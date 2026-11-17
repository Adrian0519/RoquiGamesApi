package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.DevolucionRespuestaDTO;
import Api.RoquiGames.RoquiGames.Servicios.AlquileresService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tienda/alquileres")
public class AlquileresController {

    private final AlquileresService alquileresService;

    public AlquileresController(AlquileresService alquileresService) {
        this.alquileresService = alquileresService;
    }

    @PutMapping("/{id}/devolucion")
    public DevolucionRespuestaDTO devolverAlquiler(@PathVariable Integer id) {
        return alquileresService.devolverAlquiler(id);
    }
}