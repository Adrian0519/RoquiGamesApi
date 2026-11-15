package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.Entidades.DetallesPedido;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoVenta;
import Api.RoquiGames.RoquiGames.Entidades.Ventas;
import Api.RoquiGames.RoquiGames.Repositorios.VentasRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VentasService {

    private final VentasRepository ventasRepository;

    public VentasService(VentasRepository ventasRepository) {
        this.ventasRepository = ventasRepository;
    }

    public Ventas registrarVenta(DetallesPedido detalle){

        Ventas ventas=new Ventas();

        ventas.setDetalles_pedido(detalle);
        ventas.setFecha_venta(LocalDateTime.now());
        ventas.setImporte(detalle.getPrecio_unitario());
        ventas.setEstado(EstadoVenta.REGISTRADA);

        return ventasRepository.save(ventas);

    }
}
