package webapp.kickerdb.kickerTeam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KickerTeamRepository extends JpaRepository<KickerTeam, Long> {

    Optional<KickerTeam> findByPlayerDefensiveAndPlayerOffensiveAndSide(Long playerDefensive, Long playerOffensive, int side);
}
