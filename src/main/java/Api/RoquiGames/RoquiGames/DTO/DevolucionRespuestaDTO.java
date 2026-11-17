package Api.RoquiGames.RoquiGames.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionRespuestaDTO {

    private Integer idAlquiler;
    private String nombreVideojuego;
    private String codigoEjemplar;
    private LocalDate fechaDevolucionPrevista;
    private LocalDateTime fechaDevolucionReal;
    private String estado;
    private BigDecimal fianza;
    private BigDecimal recargo;
}
