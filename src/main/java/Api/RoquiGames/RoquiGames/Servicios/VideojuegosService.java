package Api.RoquiGames.RoquiGames.Servicios;

import Api.RoquiGames.RoquiGames.DTO.CrearVideojuegoDTO;
import Api.RoquiGames.RoquiGames.DTO.MostrarVideojuegoDTO;
import Api.RoquiGames.RoquiGames.Entidades.*;
import Api.RoquiGames.RoquiGames.Especificaciones.VideojuegosSpecifications;
import Api.RoquiGames.RoquiGames.Excepciones.PeticionInvalidaException;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoDuplicadoException;
import Api.RoquiGames.RoquiGames.Excepciones.RecursoNoEncontradoException;
import Api.RoquiGames.RoquiGames.Repositorios.GenerosRepository;
import Api.RoquiGames.RoquiGames.Repositorios.PlataformasRepository;
import Api.RoquiGames.RoquiGames.Repositorios.VideojuegosGenerosRepository;
import Api.RoquiGames.RoquiGames.Repositorios.VideojuegosRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideojuegosService {

    private VideojuegosRepository videojuegosRepository;
    private PlataformasRepository plataformasRepository;
    private GenerosRepository generosRepository;
    private VideojuegosGenerosRepository videojuegosGenerosRepository;

    public VideojuegosService(VideojuegosRepository videojuegosRepository, PlataformasRepository plataformasRepository, GenerosRepository generosRepository, VideojuegosGenerosRepository videojuegosGenerosRepository) {
        this.videojuegosRepository = videojuegosRepository;
        this.plataformasRepository = plataformasRepository;
        this.generosRepository = generosRepository;
        this.videojuegosGenerosRepository = videojuegosGenerosRepository;
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

    @Transactional
    public MostrarVideojuegoDTO  crearVideojuego(CrearVideojuegoDTO crearVideojuegoDTO) {

        if (crearVideojuegoDTO.getTitulo()==null || crearVideojuegoDTO.getIdPlataforma()==null || crearVideojuegoDTO.getCodigoProducto()==null || crearVideojuegoDTO.getIdsGeneros() == null || crearVideojuegoDTO.getIdsGeneros().isEmpty()){

            throw new PeticionInvalidaException("El titulo , la plataforma y el codigo de producto son obligatorios ");

        }

        if (Boolean.TRUE.equals(crearVideojuegoDTO.getDisponibleAlquiler()) && crearVideojuegoDTO.getPrecioAlquiler() == null) {

            throw new PeticionInvalidaException("No puedes poner un juego a alquilar sin precio");

        }

        if (Boolean.TRUE.equals(crearVideojuegoDTO.getDisponibleCompra()) && crearVideojuegoDTO.getPrecioCompra() == null) {

            throw new PeticionInvalidaException("No puedes poner un juego a vender sin precio");

        }

        if (videojuegosRepository.findByCodigoProducto(crearVideojuegoDTO.getCodigoProducto()).isPresent()) {
            throw new RecursoDuplicadoException("El codigo del producto esta duplicado");
        }

        if (!plataformasRepository.existsById(crearVideojuegoDTO.getIdPlataforma())) {

            throw new RecursoNoEncontradoException("Esa id no concuerda con ninguna existente");

        }

        Plataformas plataforma=plataformasRepository.getReferenceById(crearVideojuegoDTO.getIdPlataforma());

        Videojuegos videojuego=new Videojuegos();
        videojuego.setCodigoProducto(crearVideojuegoDTO.getCodigoProducto());
        videojuego.setTitulo(crearVideojuegoDTO.getTitulo());
        videojuego.setDescripcion(crearVideojuegoDTO.getDescripcion());
        videojuego.setAnio_lanzamiento(crearVideojuegoDTO.getAnioLanzamiento());
        videojuego.setPlataforma(plataforma);
        videojuego.setPrecio_compra(crearVideojuegoDTO.getPrecioCompra());
        videojuego.setPrecio_alquiler(crearVideojuegoDTO.getPrecioAlquiler());

        if (crearVideojuegoDTO.getDisponibleCompra() != null) {
            videojuego.setDisponible_compra(crearVideojuegoDTO.getDisponibleCompra());
        }

        if (crearVideojuegoDTO.getDisponibleAlquiler() != null) {
            videojuego.setDisponible_alquiler(crearVideojuegoDTO.getDisponibleAlquiler());
        }

        videojuegosRepository.save(videojuego);

        List<VideojuegosGeneros> listaGeneros = new ArrayList<>();
        for (int i = 0; i < crearVideojuegoDTO.getIdsGeneros().size(); i++) {
            Integer idGenero = crearVideojuegoDTO.getIdsGeneros().get(i);

            if (generosRepository.findById(idGenero).isEmpty()) {

                throw new RecursoNoEncontradoException("La id del genero no esta registrada");

            }

            Generos genero = generosRepository.findById(idGenero).get();

            VideojuegosGeneros videojuegosGeneros = new VideojuegosGeneros();
            videojuegosGeneros.setId(new VideojuegosGenerosId(videojuego.getId_videojuego(), genero.getId_genero()));
            videojuegosGeneros.setVideojuegos(videojuego);
            videojuegosGeneros.setGeneros(genero);

            listaGeneros.add(videojuegosGenerosRepository.save(videojuegosGeneros));

        }

        videojuego.setVideojuegos_generos(listaGeneros);
        return convertirADTO(videojuego);
    }

    public List<MostrarVideojuegoDTO> listarVideojuegos(String titulo, List<String> plataformas, List<String> generos, String modalidad, BigDecimal precioMin, BigDecimal precioMax) {
        Specification<Videojuegos> specificaciones = VideojuegosSpecifications.estaActivo();

        if (titulo != null && !titulo.isBlank()) {
            specificaciones = specificaciones.and(VideojuegosSpecifications.tituloContiene(titulo.trim()));
        }

        if (plataformas != null && !plataformas.isEmpty()) {
            specificaciones = specificaciones.and(VideojuegosSpecifications.plataformaEn(plataformas));
        }

        if (generos != null && !generos.isEmpty()) {
            for (int i = 0; i < generos.size(); i++) {
                specificaciones = specificaciones.and(VideojuegosSpecifications.tieneGenero(generos.get(i)));
            }
        }

        if ("compra".equals(modalidad)) {
            specificaciones = specificaciones.and(VideojuegosSpecifications.disponibleParaComprar());
        } else if ("alquiler".equals(modalidad)) {
            specificaciones = specificaciones.and(VideojuegosSpecifications.disponibleParaAlquilar());
        }

        if (precioMin != null || precioMax != null) {
            specificaciones = specificaciones.and(filtroPrecio(modalidad, precioMin, precioMax));
        }

        List<Videojuegos> juegos = videojuegosRepository.findAll(specificaciones);
        List<MostrarVideojuegoDTO> resultado = new ArrayList<>();

        for (int i = 0; i < juegos.size(); i++) {
            resultado.add(convertirADTO(juegos.get(i)));
        }

        return resultado;
    }
}
