package Api.RoquiGames.RoquiGames.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarUsuarioDTO {

    private String nombre;
    private String apellidos;
    private String email;
    private String password;

}
