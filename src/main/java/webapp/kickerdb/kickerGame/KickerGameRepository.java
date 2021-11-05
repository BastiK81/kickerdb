package webapp.kickerdb.kickerGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webapp.kickerdb.kickerTeam.KickerTeam;

import java.util.List;

@Repository
public interface KickerGameRepository extends JpaRepository<KickerGame, Long> {

    @Override
    List<KickerGame> findAll();

    List<KickerGame> findByTeamOne(Long teamOne);
    List<KickerGame> findByTeamTwo(Long teamTwo);
    List<KickerGame> findByTeamOneOrTeamTwo(long teamOne, long TeamTwo);
}
