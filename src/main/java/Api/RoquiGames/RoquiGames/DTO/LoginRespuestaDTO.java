package Api.RoquiGames.RoquiGames.DTO;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespuestaDTO {

    private Integer id_usuario;
    private String nick;
    private RolUsuario rol;
    private String token;

}
