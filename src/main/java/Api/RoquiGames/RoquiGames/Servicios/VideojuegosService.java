package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.MostrarVideojuegoDTO;
import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import Api.RoquiGames.RoquiGames.Especificaciones.VideojuegosSpecifications;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoNoEncontradoException;
import Api.RoquiGames.RoquiGames.Repositorios.VideojuegosRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<MostrarVideojuegoDTO> listarVideojuegos(List<String> plataformas, List<String> generos, String modalidad, BigDecimal precioMin, BigDecimal precioMax) {
        Specification<Videojuegos> spec = VideojuegosSpecifications.estaActivo();

        if (plataformas != null && !plataformas.isEmpty()) {
            spec = spec.and(VideojuegosSpecifications.plataformaEn(plataformas));
        }

        if (generos != null && !generos.isEmpty()) {
            for (int i = 0; i < generos.size(); i++) {
                spec = spec.and(VideojuegosSpecifications.tieneGenero(generos.get(i)));
            }
        }

        if ("compra".equals(modalidad)) {
            spec = spec.and(VideojuegosSpecifications.disponibleParaComprar());
        } else if ("alquiler".equals(modalidad)) {
            spec = spec.and(VideojuegosSpecifications.disponibleParaAlquilar());
        }

        if (precioMin != null || precioMax != null) {
            spec = spec.and(filtroPrecio(modalidad, precioMin, precioMax));
        }

        List<Videojuegos> juegos = videojuegosRepository.findAll(spec);
        List<MostrarVideojuegoDTO> resultado = new ArrayList<>();

        for (int i = 0; i < juegos.size(); i++) {
            resultado.add(convertirADTO(juegos.get(i)));
        }

        return resultado;
    }

    private Specification<Videojuegos> filtroPrecio(String modalidad, BigDecimal precioMin, BigDecimal precioMax) {

        // Condición vacía (no filtra nada): hace el papel del antiguo where(null),
        // pero sin ser null, así que se le puede encadenar .and() / .or() sin errores.
        // Spring Data JPA 4 ya no admite null como Specification.
        Specification<Videojuegos> compra = Specification.unrestricted();
        if (precioMin != null) {
            compra = compra.and(VideojuegosSpecifications.precioCompraDesde(precioMin));
        }
        if (precioMax != null) {
            compra = compra.and(VideojuegosSpecifications.precioCompraHasta(precioMax));
        }

        Specification<Videojuegos> alquiler = Specification.unrestricted();
        if (precioMin != null) {
            alquiler = alquiler.and(VideojuegosSpecifications.precioAlquilerDesde(precioMin));
        }
        if (precioMax != null) {
            alquiler = alquiler.and(VideojuegosSpecifications.precioAlquilerHasta(precioMax));
        }

        if ("compra".equals(modalidad)) {

            return compra;

        } else if ("alquiler".equals(modalidad)) {

            return alquiler;

        } else {

            return compra.or(alquiler);

        }
    }

    public MostrarVideojuegoDTO obtenerVideojuego(Integer idVideojuego) {

        Videojuegos videojuego = videojuegosRepository.findById(idVideojuego)
                .orElseThrow(() -> new RecursoNoEncontradoException("El videojuego no existe"));

        if (!videojuego.getActivo()) {
            throw new RecursoNoEncontradoException("El videojuego no existe");
        }

        return convertirADTO(videojuego);

    }
}
