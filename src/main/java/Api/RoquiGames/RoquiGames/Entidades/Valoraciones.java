package Api.RoquiGames.RoquiGames.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "valoraciones")
public class Valoraciones {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracion")
    private Integer id_valoracion;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuarios usuarios;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_videojuego", nullable = false)
    private Videojuegos videojuegos;

    @Column(nullable = false)
    private Byte puntuacion;

    @Column(columnDefinition = "TEXT")
    private String critica;

    @Column(name = "fecha_valoracion", insertable = false, updatable = false)
    private LocalDateTime fecha_valoracion;
}
