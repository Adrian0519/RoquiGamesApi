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
public class CrearVideojuegoDTO {

    private String codigoProducto;
    private String titulo;
    private String descripcion;
    private Integer anioLanzamiento;
    private Integer idPlataforma;
    private BigDecimal precioCompra;
    private BigDecimal precioAlquiler;
    private Boolean disponibleCompra;
    private Boolean disponibleAlquiler;
    private List<Integer> idsGeneros;
}
