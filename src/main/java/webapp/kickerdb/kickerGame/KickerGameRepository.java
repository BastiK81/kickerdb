package webapp.kickerdb.kickerGame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KickerGameRepository extends JpaRepository<KickerGame, Long> {
}
