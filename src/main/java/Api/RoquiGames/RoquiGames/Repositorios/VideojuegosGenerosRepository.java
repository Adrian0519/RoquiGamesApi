package Api.RoquiGames.RoquiGames.Repositorios;

import Api.RoquiGames.RoquiGames.Entidades.VideojuegosGeneros;
import Api.RoquiGames.RoquiGames.Entidades.VideojuegosGenerosId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideojuegosGenerosRepository extends JpaRepository<VideojuegosGeneros, VideojuegosGenerosId> {

}
