package Api.RoquiGames.RoquiGames.DTO;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.TipoOperacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineaPedidoDTO {

    private Integer idVideojuego;
    private TipoOperacion tipo;
    private Integer dias;

}
