package Api.RoquiGames.RoquiGames.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "videojuegos")
public class Videojuegos {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_videojuego")
    private Integer id_videojuego;

    @Column(name = "codigo_producto", nullable = false, unique = true, length = 50)
    private String codigo_producto;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "anio_lanzamiento")
    private Integer anio_lanzamiento;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_plataforma", nullable = false)
    private Plataformas plataforma;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precio_compra;

    @Column(name = "precio_alquiler", precision = 10, scale = 2)
    private BigDecimal precio_alquiler;

    @Column(name = "disponible_compra", nullable = false)
    private Boolean disponible_compra = true;

    @Column(name = "disponible_alquiler", nullable = false)
    private Boolean disponible_alquiler = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @ToString.Exclude
    @OneToMany(mappedBy = "videojuegos")
    private List<VideojuegosGeneros> videojuegos_generos;

    @ToString.Exclude
    @OneToMany(mappedBy = "videojuegos")
    private List<Ejemplares> ejemplares;

    @ToString.Exclude
    @OneToMany(mappedBy = "videojuegos")
    private List<Valoraciones> valoraciones;
}