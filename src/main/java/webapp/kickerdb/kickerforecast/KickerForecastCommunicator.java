package webapp.kickerdb.kickerforecast;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KickerForecastCommunicator {

    @Autowired
    private KickerForecastRepository repository;

    public List<KickerForecastGameItem> getAllPossibleGames() {
        return repository.findAll();
    }

    public int getGameCountWithTeamOneIdAndTeamTwoId(Long idOne, Long idTwo) {
        return repository.findByTeamOneIdAndTeamTwoId(idOne, idTwo).size();
    }
}
