package Api.RoquiGames.RoquiGames.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MostrarValoracionesDTO {

    private String nombreUsuario;
    private Byte puntuacion;
    private String critica;
    private LocalDateTime fecha_valoracion;

}
