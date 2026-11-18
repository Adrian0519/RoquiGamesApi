package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.CrearPedidoDTO;
import Api.RoquiGames.RoquiGames.DTO.LineaPedidoDTO;
import Api.RoquiGames.RoquiGames.DTO.LineaPedidoRespuestaDTO;
import Api.RoquiGames.RoquiGames.DTO.PedidoRespuestaDTO;
import Api.RoquiGames.RoquiGames.Entidades.*;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoEjemplar;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoPedido;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.TipoOperacion;
import Api.RoquiGames.RoquiGames.Excepciones.PeticionInvalidaException;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoNoEncontradoException;
import Api.RoquiGames.RoquiGames.Repositorios.DetallesPedidoRepository;
import Api.RoquiGames.RoquiGames.Repositorios.PedidosRepository;
import Api.RoquiGames.RoquiGames.Repositorios.VideojuegosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidosService {

    private final PedidosRepository pedidosRepository;
    private final DetallesPedidoRepository detallesPedidoRepository;
    private final VideojuegosRepository videojuegosRepository;
    private final EjemplaresService ejemplaresService;
    private final VentasService ventasService;
    private final AlquileresService alquileresService;

    public PedidosService(PedidosRepository pedidosRepository, DetallesPedidoRepository detallesPedidoRepository, VideojuegosRepository videojuegosRepository, EjemplaresService ejemplaresService, VentasService ventasService, AlquileresService alquileresService) {
        this.pedidosRepository = pedidosRepository;
        this.detallesPedidoRepository = detallesPedidoRepository;
        this.videojuegosRepository = videojuegosRepository;
        this.ejemplaresService = ejemplaresService;
        this.ventasService = ventasService;
        this.alquileresService = alquileresService;
    }

    private void validarCarrito(CrearPedidoDTO crearPedidoDTO) {

        if (crearPedidoDTO==null || crearPedidoDTO.getLineas().isEmpty()){

            throw new PeticionInvalidaException("El carrito esta vacio");

        }
    }

    private Pedidos crearPedidoPendiente(Usuarios usuario) {

        Pedidos pedido=new Pedidos();

        pedido.setUsuarios(usuario);
        pedido.setFecha_pedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setImporte_total(BigDecimal.ZERO);

        return pedidosRepository.save(pedido);
    }

    private Videojuegos buscarJuegoActivo(Integer idVideojuego) {

        Videojuegos videojuego = videojuegosRepository.findById(idVideojuego)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el videojuego"));

        if (!videojuego.getActivo()) {
            throw new PeticionInvalidaException("Ese juego no esta disponible");
        }

        return videojuego;
    }

    private DetallesPedido crearDetalle(Pedidos pedido, Videojuegos videojuego, Ejemplares ejemplar, TipoOperacion tipo, BigDecimal precio) {

        DetallesPedido detallesPedido=new DetallesPedido();
       detallesPedido.setPedidos(pedido);
       detallesPedido.setVideojuegos(videojuego);
       detallesPedido.setEjemplares(ejemplar);
       detallesPedido.setTipo_operacion(tipo);
       detallesPedido.setPrecio_unitario(precio);
       detallesPedido.setCodigo_producto(videojuego.getCodigoProducto());
       detallesPedido.setNombre_videojuego(videojuego.getTitulo());
       detallesPedido.setCodigo_ejemplar(ejemplar.getCodigo_ejemplar());

       return detallesPedidoRepository.save(detallesPedido);
    }

    private DetallesPedido procesarCompra(Pedidos pedido, Videojuegos videojuego) {

        if (!videojuego.getDisponible_compra()){
            throw new PeticionInvalidaException("El juego no se puede comprar");
        }

        Ejemplares ejemplar= ejemplaresService.reservarEjemplar(videojuego, EstadoEjemplar.VENDIDO);
        DetallesPedido detalle = crearDetalle(pedido, videojuego, ejemplar, TipoOperacion.COMPRA, videojuego.getPrecio_compra());
        ventasService.registrarVenta(detalle);

        return detalle;

    }

    private DetallesPedido procesarLinea(Pedidos pedido, LineaPedidoDTO linea) {
        Videojuegos videojuego = buscarJuegoActivo(linea.getIdVideojuego());

        if (linea.getTipo() == TipoOperacion.COMPRA) {
            return procesarCompra(pedido, videojuego);
        }

        return procesarAlquiler(pedido, videojuego, linea.getDias());
    }


    private void confirmarPedido(Pedidos pedido, List<DetallesPedido> detalles) {

        BigDecimal total=BigDecimal.ZERO;

        for (int i = 0; i < detalles.size(); i++) {
            total = total.add(detalles.get(i).getPrecio_unitario());
        }

        pedido.setEstado(EstadoPedido.CONFIRMADO);
        pedido.setImporte_total(total);
        pedidosRepository.save(pedido);
    }

    private PedidoRespuestaDTO convertirARespuesta(Pedidos pedido, List<DetallesPedido> detalles) {

        List<LineaPedidoRespuestaDTO> lineas = new ArrayList<>();

        for (int i = 0; i < detalles.size(); i++) {
            DetallesPedido detalle = detalles.get(i);

            lineas.add(LineaPedidoRespuestaDTO.builder()
                    .nombreVideojuego(detalle.getNombre_videojuego())
                    .codigoEjemplar(detalle.getCodigo_ejemplar())
                    .tipo(detalle.getTipo_operacion().name())
                    .precio(detalle.getPrecio_unitario())
                    .build());
        }

        return PedidoRespuestaDTO.builder()
                .idPedido(pedido.getId_pedido())
                .fecha(pedido.getFecha_pedido())
                .estado(pedido.getEstado().name())
                .importeTotal(pedido.getImporte_total())
                .lineas(lineas)
                .build();
    }

    @Transactional
    public PedidoRespuestaDTO crearPedido(Usuarios usuario, CrearPedidoDTO crearPedidoDTO) {
       validarCarrito(crearPedidoDTO);
       Pedidos pedidos= crearPedidoPendiente(usuario);
       List<DetallesPedido>detallesPedidoList=new ArrayList<>();

        for (int i = 0; i < crearPedidoDTO.getLineas().size(); i++) {
            LineaPedidoDTO linea = crearPedidoDTO.getLineas().get(i);
            DetallesPedido detalle = procesarLinea(pedidos, linea);
            detallesPedidoList.add(detalle);
        }
        confirmarPedido(pedidos,detallesPedidoList);
        return convertirARespuesta(pedidos,detallesPedidoList);
    }

    private DetallesPedido procesarAlquiler(Pedidos pedido, Videojuegos videojuego, Integer dias){

        if (dias == null || dias <= 0) {
            throw new PeticionInvalidaException("Los dias de alquiler no pueden ser 0");
        }

        if (!videojuego.getDisponible_alquiler()){
            throw new PeticionInvalidaException("El juego no se puede alquilar");
        }

        Ejemplares ejemplar= ejemplaresService.reservarEjemplar(videojuego, EstadoEjemplar.ALQUILADO);
        BigDecimal precioTotal = videojuego.getPrecio_alquiler().multiply(BigDecimal.valueOf(dias));
        DetallesPedido detalle = crearDetalle(pedido, videojuego, ejemplar, TipoOperacion.ALQUILER, precioTotal);
        alquileresService.registrarAlquiler(detalle,dias);

        return detalle;

    }
}
