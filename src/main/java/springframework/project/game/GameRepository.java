package springframework.project.game;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game,Integer>{
    
    @Query("SELECT g FROM Game g WHERE g.code = ?1")
    Optional<Game> findByGameCode(String code) throws DataAccessException;
}
