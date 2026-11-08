package Api.RoquiGames.RoquiGames.Entidades;

import Api.RoquiGames.RoquiGames.Entidades.Numerados.RolUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "usuarios")
public class Usuarios implements UserDetails {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id_usuario;

    @Column(nullable = false, length = 3072)
    private String nombre;

    @Column(length = 3072)
    private String apellidos;

    @Column(nullable = false, length = 3072)
    private String email;

    @Column(name = "email_hash", nullable = false, unique = true, length = 64)
    private String emailHash;

    @Column(nullable = false, unique = true, length = 50)
    private String nick;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + rol.name())
        );
    }

    @Override
    public String getUsername() {
        return nick;
    }

    //ESTOS 4 METODOS SON DE SPRING

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(activo);
    }
}
