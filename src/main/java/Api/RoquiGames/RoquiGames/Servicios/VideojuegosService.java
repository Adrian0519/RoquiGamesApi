package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.MostrarVideojuegoDTO;
import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import Api.RoquiGames.RoquiGames.Repositorios.VideojuegosRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideojuegosService {

    private VideojuegosRepository videojuegosRepository;

    public VideojuegosService(VideojuegosRepository videojuegosRepository) {
        this.videojuegosRepository = videojuegosRepository;
    }

    private MostrarVideojuegoDTO convertirADTO(Videojuegos videojuegos) {

        return MostrarVideojuegoDTO.builder()
                .id_videojuego(videojuegos.getId_videojuego())
                .titulo(videojuegos.getTitulo())
                .descripcion(videojuegos.getDescripcion())
                .anio_lanzamiento(videojuegos.getAnio_lanzamiento())
                .plataforma(videojuegos.getPlataforma().getNombre())
                .precio_compra(videojuegos.getPrecio_compra())
                .precio_alquiler(videojuegos.getPrecio_alquiler())
                .disponible_compra(videojuegos.getDisponible_compra())
                .disponible_alquiler(videojuegos.getDisponible_alquiler())
                .generos(videojuegos.getVideojuegos_generos().stream()
                        .map(vg -> vg.getGeneros().getNombre())
                        .toList())
                .build();

    }

    public List<MostrarVideojuegoDTO> listarVideojuegos() {

        List<Videojuegos> juegos = videojuegosRepository.findByActivoTrue();
        List<MostrarVideojuegoDTO> resultado = new ArrayList<>();

        for (int i = 0; i < juegos.size(); i++) {
            resultado.add(convertirADTO(juegos.get(i)));
        }

        return resultado;

    }
}
