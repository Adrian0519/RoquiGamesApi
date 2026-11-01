package Api.RoquiGames.RoquiGames.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "videojuegos_generos")
public class VideojuegosGeneros {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private VideojuegosGenerosId id;

    @ToString.Exclude
    @ManyToOne
    @MapsId("id_videojuego")
    @JoinColumn(name = "id_videojuego")
    private Videojuegos videojuegos;

    @ToString.Exclude
    @ManyToOne
    @MapsId("id_genero")
    @JoinColumn(name = "id_genero")
    private Generos generos;
}
