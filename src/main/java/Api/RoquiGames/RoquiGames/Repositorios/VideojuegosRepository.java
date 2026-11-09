package Api.RoquiGames.RoquiGames.Repositorios;

import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideojuegosRepository extends JpaRepository<Videojuegos, Integer> {

    public List<Videojuegos> findByActivoTrue();

}
