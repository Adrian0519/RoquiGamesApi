package Api.RoquiGames.RoquiGames.Repositorios;

import Api.RoquiGames.RoquiGames.Entidades.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {

    Optional<Usuarios> findByEmailHash(String emailHash);

}
