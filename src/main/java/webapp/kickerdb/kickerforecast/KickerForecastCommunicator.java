package webapp.kickerdb.kickerforecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KickerForecastCommunicator {

    @Autowired
    private KickerForecastRepository repository;

    public List<KickerForecastGameItem> getAllPossibleGames() {
        return repository.findAll();
    }

    public boolean hasGameWithTeamIdOneAndTeamIdTwo(Long idTeamOne, Long idTeamTwo) {
        return repository.findByTeamOneIdAndTeamTwoId(idTeamOne, idTeamTwo).isEmpty();
    }

    public void saveKickerforecastGameItem(KickerForecastGameItem game) {
        repository.save(game);

    }
}
