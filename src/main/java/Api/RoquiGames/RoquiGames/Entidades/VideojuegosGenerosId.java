package Api.RoquiGames.RoquiGames.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VideojuegosGenerosId implements Serializable {

    @Column(name = "id_videojuego")
    private Integer id_videojuego;

    @Column(name = "id_genero")
    private Integer id_genero;
}
