package Api.RoquiGames.RoquiGames.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "generos")
public class Generos {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero")
    private Integer id_genero;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @ToString.Exclude
    @OneToMany(mappedBy = "generos")
    private List<VideojuegosGeneros> videojuegos_generos;
}
