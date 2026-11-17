package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.DevolucionRespuestaDTO;
import Api.RoquiGames.RoquiGames.Entidades.Alquileres;
import Api.RoquiGames.RoquiGames.Entidades.DetallesPedido;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoAlquiler;
import Api.RoquiGames.RoquiGames.Excepciones.PeticionInvalidaException;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoNoEncontradoException;
import Api.RoquiGames.RoquiGames.Repositorios.AlquileresRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AlquileresService {

    private final AlquileresRepository alquileresRepository;
    private final EjemplaresService ejemplaresService;

    public AlquileresService(AlquileresRepository alquileresRepository, EjemplaresService ejemplaresService) {
        this.alquileresRepository = alquileresRepository;
        this.ejemplaresService = ejemplaresService;
    }

    public Alquileres registrarAlquiler(DetallesPedido detalle, Integer dias) {

        Alquileres alquiler = new Alquileres();

        alquiler.setDetalles_pedido(detalle);
        alquiler.setFecha_devolucion_prevista(LocalDate.now().plusDays(dias));
        alquiler.setEstado(EstadoAlquiler.ACTIVO);

        return alquileresRepository.save(alquiler);

    }

    private BigDecimal calcularRecargo(Alquileres alquiler) {
        long diasRetraso = ChronoUnit.DAYS.between(
                alquiler.getFecha_devolucion_prevista(),
                alquiler.getFecha_devolucion_real().toLocalDate());
        BigDecimal tasa = BigDecimal.ZERO;

        if (diasRetraso <= 0) {
            return BigDecimal.ZERO;
        }

        if (diasRetraso <= 5) {
            tasa = new BigDecimal("1.30");
        } else if (diasRetraso <= 10) {
            tasa = new BigDecimal("1.35");
        } else if (diasRetraso <= 15) {
            tasa = new BigDecimal("1.40");
        } else if (diasRetraso <= 20) {
            tasa = new BigDecimal("1.45");
        } else {
            tasa = new BigDecimal("1.50");
        }

        BigDecimal precio = alquiler.getDetalles_pedido().getVideojuegos().getPrecio_alquiler();

        return precio.multiply(tasa).multiply(BigDecimal.valueOf(diasRetraso)).setScale(2, RoundingMode.HALF_UP);

    }

    @Transactional
    public DevolucionRespuestaDTO devolverAlquiler(Integer idAlquiler) {

        Alquileres alquileres = alquileresRepository.findById(idAlquiler).orElse(null);

        if (alquileres==null){
            throw new RecursoNoEncontradoException("No hay ningun alquiler con ese id ");
        }

        if (alquileres.getEstado()!=EstadoAlquiler.ACTIVO){
            throw new PeticionInvalidaException("No se puede devolver ");
        }


        alquileres.setFecha_devolucion_real(LocalDateTime.now());
        alquileres.setRecargo(calcularRecargo(alquileres));
        alquileres.setEstado(EstadoAlquiler.DEVUELTO);

        return convertirARespuesta(alquileresRepository.save(alquileres));

    }

    private DevolucionRespuestaDTO convertirARespuesta(Alquileres alquiler) {
        DetallesPedido detalle = alquiler.getDetalles_pedido();

        return DevolucionRespuestaDTO.builder()
                .idAlquiler(alquiler.getId_alquiler())
                .nombreVideojuego(detalle.getNombre_videojuego())
                .codigoEjemplar(detalle.getCodigo_ejemplar())
                .fechaDevolucionPrevista(alquiler.getFecha_devolucion_prevista())
                .fechaDevolucionReal(alquiler.getFecha_devolucion_real())
                .estado(alquiler.getEstado().name())
                .fianza(alquiler.getFianza())
                .recargo(alquiler.getRecargo())
                .build();
    }


}
