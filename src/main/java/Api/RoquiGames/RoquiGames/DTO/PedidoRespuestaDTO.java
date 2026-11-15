package Api.RoquiGames.RoquiGames.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRespuestaDTO {

    private Integer idPedido;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal importeTotal;
    private List<LineaPedidoRespuestaDTO> lineas;
}
