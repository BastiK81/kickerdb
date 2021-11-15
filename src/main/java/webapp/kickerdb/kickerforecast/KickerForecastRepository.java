package webapp.kickerdb.kickerforecast;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KickerForecastRepository extends JpaRepository<KickerForecastGameItem, Long> {

    List<KickerForecastGameItem> findByTeamOneIdAndTeamTwoId(long idOne, long idTwo);
}
