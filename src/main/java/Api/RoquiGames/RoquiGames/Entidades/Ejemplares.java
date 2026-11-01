package Api.RoquiGames.RoquiGames.Entidades;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoEjemplar;
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
@Table(name = "ejemplares")
public class Ejemplares {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejemplar")
    private Integer id_ejemplar;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_videojuego", nullable = false)
    private Videojuegos videojuegos;

    @Column(name = "codigo_ejemplar", nullable = false, unique = true, length = 60)
    private String codigo_ejemplar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEjemplar estado = EstadoEjemplar.DISPONIBLE;

    @Column(length = 500)
    private String observaciones;
}
