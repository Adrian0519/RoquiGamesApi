package Api.RoquiGames.RoquiGames.Repositorios;

import Api.RoquiGames.RoquiGames.Entidades.Ejemplares;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoEjemplar;
import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EjemplaresRepository extends JpaRepository<Ejemplares, Integer> {

    Optional<Ejemplares> findFirstByVideojuegosAndEstado(Videojuegos videojuegos, EstadoEjemplar estado);

}
