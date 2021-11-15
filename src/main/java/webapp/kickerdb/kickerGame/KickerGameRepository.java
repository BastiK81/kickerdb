package webapp.kickerdb.kickerGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KickerGameRepository extends JpaRepository<KickerGame, Long> {

    List<KickerGame> findByTeamOne(Long teamOne);
    List<KickerGame> findByTeamTwo(Long teamTwo);
    List<KickerGame> findByTeamOneOrTeamTwo(long teamOne, long TeamTwo);
    List<KickerGame> findByTeamOneAndTeamTwo(long teamOne, long TeamTwo);
}
