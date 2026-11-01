package Api.RoquiGames.RoquiGames.Entidades;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.TipoOperacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "detalles_pedido")
public class DetallesPedido {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id_detalle;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedidos pedidos;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_videojuego", nullable = false)
    private Videojuegos videojuegos;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_ejemplar", nullable = false)
    private Ejemplares ejemplares;

    @Column(name = "codigo_producto", nullable = false, length = 50)
    private String codigo_producto;

    @Column(name = "codigo_ejemplar", nullable = false, length = 60)
    private String codigo_ejemplar;

    @Column(name = "nombre_videojuego", nullable = false, length = 200)
    private String nombre_videojuego;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacion", nullable = false)
    private TipoOperacion tipo_operacion;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio_unitario;

    @ToString.Exclude
    @OneToOne(mappedBy = "detalles_pedido")
    private Ventas ventas;

    @ToString.Exclude
    @OneToOne(mappedBy = "detalles_pedido")
    private Alquileres alquileres;
}