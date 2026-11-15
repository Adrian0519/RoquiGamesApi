package Api.RoquiGames.RoquiGames.Controladores;

import Api.RoquiGames.RoquiGames.DTO.CrearPedidoDTO;
import Api.RoquiGames.RoquiGames.DTO.PedidoRespuestaDTO;
import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import Api.RoquiGames.RoquiGames.Servicios.PedidosService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente/pedidos")
public class PedidosController {

    private final PedidosService pedidosService;

    public PedidosController(PedidosService pedidosService) {

        this.pedidosService = pedidosService;

    }

    @PostMapping
    public PedidoRespuestaDTO crearPedido(@AuthenticationPrincipal Usuarios usuario, @RequestBody CrearPedidoDTO crearPedidoDTO) {

        return pedidosService.crearPedido(usuario, crearPedidoDTO);

    }
}
