package Api.RoquiGames.RoquiGames.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearVideojuegoDTO {

    private String codigo_producto;
    private String titulo;
    private String descripcion;
    private Integer anio_lanzamiento;
    private String nombre_plataforma;
    private BigDecimal precio_compra;
    private BigDecimal precio_alquiler;
    private Boolean disponible_compra;
    private Boolean disponible_alquiler;
    private Boolean activo;
    private List<String> generos;

}
