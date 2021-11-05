package webapp.kickerdb.kickerTeam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KickerTeamRepository extends JpaRepository<KickerTeam, Long> {

    Optional<KickerTeam> findByPlayerDefensiveAndPlayerOffensiveAndSide(Long playerDefensive, Long playerOffensive, int side);

    List<KickerTeam> findByPlayerDefensiveOrPlayerOffensive(Long idDefensive, Long idOffensive);
}
