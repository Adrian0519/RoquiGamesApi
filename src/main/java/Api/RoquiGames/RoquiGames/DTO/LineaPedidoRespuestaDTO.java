package Api.RoquiGames.RoquiGames.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedidoRespuestaDTO {

    private String nombreVideojuego;
    private String codigoEjemplar;
    private String tipo;
    private BigDecimal precio;

}
