package Api.RoquiGames.RoquiGames.Entidades;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoVenta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ventas")
public class Ventas {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer id_venta;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "id_detalle", nullable = false, unique = true)
    private DetallesPedido detalles_pedido;

    @Column(name = "fecha_venta", insertable = false, updatable = false)
    private LocalDateTime fecha_venta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado = EstadoVenta.REGISTRADA;
}
