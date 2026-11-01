package Api.RoquiGames.RoquiGames.Entidades;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.RolUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id_usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 150)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @ToString.Exclude
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol = RolUsuario.CLIENTE;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fecha_registro;

    @Column(nullable = false)
    private Boolean activo = true;

    @ToString.Exclude
    @OneToMany(mappedBy = "usuarios")
    private List<Pedidos> pedidos;

    @ToString.Exclude
    @OneToMany(mappedBy = "usuarios")
    private List<Valoraciones> valoraciones;
}
