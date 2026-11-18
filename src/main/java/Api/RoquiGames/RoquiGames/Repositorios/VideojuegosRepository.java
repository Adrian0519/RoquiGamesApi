package Api.RoquiGames.RoquiGames.Repositorios;

import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface VideojuegosRepository extends JpaRepository<Videojuegos, Integer> , JpaSpecificationExecutor<Videojuegos> {

    public List<Videojuegos> findByActivoTrue();

    Optional<Videojuegos> findByCodigoProducto(String codigoProducto);

}
