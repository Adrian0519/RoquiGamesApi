package Api.RoquiGames.RoquiGames.DTO;

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

    private Integer id_videojuego;
    private String titulo;
    private String descripcion;
    private Integer anio_lanzamiento;
    private String plataforma;
    private BigDecimal precio_compra;
    private BigDecimal precio_alquiler;
    private Boolean disponible_compra;
    private Boolean disponible_alquiler;
    private List<String> generos;
}
