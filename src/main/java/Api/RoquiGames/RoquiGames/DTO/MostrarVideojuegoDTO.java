package Api.RoquiGames.RoquiGames.DTO;

import Api.RoquiGames.RoquiGames.Entidades.Ejemplares;
import Api.RoquiGames.RoquiGames.Entidades.Plataformas;
import Api.RoquiGames.RoquiGames.Entidades.Valoraciones;
import Api.RoquiGames.RoquiGames.Entidades.VideojuegosGeneros;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MostrarVideojuegoDTO {

    private String titulo;
    private String descripcion;
    private Integer anio_lanzamiento;
    private Plataformas plataforma;
    private BigDecimal precio_compra;
    private BigDecimal precio_alquiler;
    private List<String> videojuegos_generos;
    private Boolean disponible_compra;
    private Boolean disponible_alquiler;

}
