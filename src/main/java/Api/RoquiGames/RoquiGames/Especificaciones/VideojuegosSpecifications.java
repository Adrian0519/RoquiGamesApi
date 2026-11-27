package Api.RoquiGames.RoquiGames.Especificaciones;

import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import Api.RoquiGames.RoquiGames.Entidades.VideojuegosGeneros;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public class VideojuegosSpecifications {

    // "¿Este juego está activo?"  →  SQL: WHERE activo = true
    public static Specification<Videojuegos> estaActivo() {
        return (root, query, cb) ->         // root = el juego que se mira, cb = herramientas para preguntar
                cb.isTrue(                  // herramienta "¿es verdadero?"
                        root.get("activo")  // ...aplicada al campo "activo" del juego
                );
    }

    public static Specification<Videojuegos> disponibleParaComprar() {
        return (root, query, cb) ->
                cb.isTrue(
                        root.get("disponible_compra")
                );
    }

    public static Specification<Videojuegos> disponibleParaAlquilar() {
        return (root, query, cb) ->
                cb.isTrue(
                        root.get("disponible_alquiler")
                );
    }

    // "¿La plataforma del juego es una de estas?"  →  SQL: WHERE plataforma.nombre IN ('NES', 'SNES')
    public static Specification<Videojuegos> plataformaEn(List<String> plataformas) {
        return (root, query, cb) ->
                root.get("plataforma")      // del juego, coge su plataforma (es otro objeto: Plataformas)
                        .get("nombre")          // de esa plataforma, coge su nombre ("NES", "SNES"...)
                        .in(plataformas);       // ¿está ese nombre dentro de la lista que llega de la URL?
    }

    // "¿Este juego tiene el género X?"
    // SQL: JOIN videojuegos_generos ... JOIN generos ... WHERE generos.nombre = 'X'
    public static Specification<Videojuegos> tieneGenero(String genero) {
        return (root, query, cb) -> {       // llaves porque hay varias líneas → el return va dentro

            // El juego NO guarda sus géneros: están en la tabla intermedia videojuegos_generos.
            // join() "grapa" esa tabla al juego para poder mirar sus géneros. vg = cada fila grapada.
            Join<Videojuegos, VideojuegosGeneros> vg = root.join("videojuegos_generos");

            // Al grapar, un juego con 2 géneros aparecería 2 veces. distinct lo deja en 1.
            query.distinct(true);

            return cb.equal(                // herramienta "¿es igual a...?"
                    vg.get("generos")       // de la fila grapada, coge su género (objeto Generos)
                            .get("nombre"),       // de ese género, coge el nombre ("Aventura"...)
                    genero                  // ...y compáralo con el género que llega de la URL
            );
        };
    }

    // "¿El precio de compra es al menos min?"  →  SQL: WHERE precio_compra >= min
    // Desde = greaterThanOrEqualTo (>=)   ·   Hasta = lessThanOrEqualTo (<=)
    public static Specification<Videojuegos> precioCompraDesde(BigDecimal min) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(            // herramienta "¿es mayor o igual que...?"
                        root.get("precio_compra"),  // ...el precio de compra del juego
                        min                         // ...respecto al mínimo que llega de la URL
                );
    }

    public static Specification<Videojuegos> precioCompraHasta(BigDecimal max) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("precio_compra"),
                        max
                );
    }

    public static Specification<Videojuegos> precioAlquilerDesde(BigDecimal min) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("precio_alquiler"),
                        min
                );
    }

    public static Specification<Videojuegos> precioAlquilerHasta(BigDecimal max) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("precio_alquiler"),
                        max
                );
    }

    // "¿El título contiene este texto?"  →  SQL: WHERE LOWER(titulo) LIKE '%zelda%'
    public static Specification<Videojuegos> tituloContiene(String texto) {
        return (root, query, cb) ->
                cb.like(                                // herramienta "¿se parece a...?" (LIKE)
                        cb.lower(root.get("titulo")),   // el título del juego, en minúsculas
                        "%" + texto.toLowerCase() + "%" // ...el texto buscado, en minúsculas y con % a los lados
                );
    }
}
