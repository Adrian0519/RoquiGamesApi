package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.Entidades.Alquileres;
import Api.RoquiGames.RoquiGames.Entidades.DetallesPedido;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoAlquiler;
import Api.RoquiGames.RoquiGames.Repositorios.AlquileresRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AlquileresService {

    private final AlquileresRepository alquileresRepository;

    public AlquileresService(AlquileresRepository alquileresRepository) {
        this.alquileresRepository = alquileresRepository;
    }

    public Alquileres registrarAlquiler(DetallesPedido detalle,Integer dias){

        Alquileres alquiler=new Alquileres();

        alquiler.setDetalles_pedido(detalle);
        alquiler.setFecha_devolucion_prevista(LocalDate.now().plusDays(dias));
        alquiler.setEstado(EstadoAlquiler.ACTIVO);

        return alquileresRepository.save(alquiler);

    }
}
