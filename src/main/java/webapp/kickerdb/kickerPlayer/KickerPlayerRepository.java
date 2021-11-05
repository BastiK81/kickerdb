package webapp.kickerdb.kickerPlayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KickerPlayerRepository extends JpaRepository<KickerPlayer, Long> {

    Optional<KickerPlayer> findByUserName(String userName);

    List<KickerPlayer> findByActive(Boolean active);
}
