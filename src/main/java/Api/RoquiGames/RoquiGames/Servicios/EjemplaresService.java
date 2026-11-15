package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.Entidades.Ejemplares;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoEjemplar;
import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoDuplicadoException;
import Api.RoquiGames.RoquiGames.Repositorios.EjemplaresRepository;
import org.springframework.stereotype.Service;

@Service
public class EjemplaresService {
    private final EjemplaresRepository ejemplaresRepository;

    public EjemplaresService(EjemplaresRepository ejemplaresRepository) {
        this.ejemplaresRepository = ejemplaresRepository;
    }

    public Ejemplares reservarEjemplar(Videojuegos videojuego, EstadoEjemplar nuevoEstado) {

        Ejemplares ejemplar = ejemplaresRepository.findFirstByVideojuegosAndEstado(videojuego, EstadoEjemplar.DISPONIBLE)
                .orElseThrow(() -> new RecursoDuplicadoException("No quedan ejemplares de " + videojuego.getTitulo()));
        ejemplar.setEstado(nuevoEstado);
        return ejemplaresRepository.save(ejemplar);

    }


}
