package Api.RoquiGames.RoquiGames.Repositorios;

import Api.RoquiGames.RoquiGames.Entidades.Ejemplares;
import Api.RoquiGames.RoquiGames.Entidades.Numerados.EstadoEjemplar;
import Api.RoquiGames.RoquiGames.Entidades.Videojuegos;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface EjemplaresRepository extends JpaRepository<Ejemplares, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Ejemplares> findFirstByVideojuegosAndEstado(Videojuegos videojuegos, EstadoEjemplar estado);

}
