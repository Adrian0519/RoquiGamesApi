package Api.RoquiGames.RoquiGames.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilUsuarioDTO {

    private Integer id_usuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String nick;

}
