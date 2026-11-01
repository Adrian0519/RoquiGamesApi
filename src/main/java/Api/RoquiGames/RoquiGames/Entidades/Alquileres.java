package Api.RoquiGames.RoquiGames.Entidades;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoAlquiler;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "alquileres")
public class Alquileres {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Integer id_alquiler;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "id_detalle", nullable = false, unique = true)
    private DetallesPedido detalles_pedido;

    @Column(name = "fecha_inicio", insertable = false, updatable = false)
    private LocalDateTime fecha_inicio;

    @Column(name = "fecha_devolucion_prevista", nullable = false)
    private LocalDate fecha_devolucion_prevista;

    @Column(name = "fecha_devolucion_real")
    private LocalDateTime fecha_devolucion_real;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlquiler estado = EstadoAlquiler.ACTIVO;
}
