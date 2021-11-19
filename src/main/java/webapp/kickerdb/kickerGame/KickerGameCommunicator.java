package webapp.kickerdb.kickerGame;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class KickerGameCommunicator {

    @Autowired
    private KickerGameRepository repository;

    public Long getSaveGameId(KickerGame kickerGame) {
        return repository.save(kickerGame).getId();
    }

    public List<KickerGame> getAllGamesWithTeamId(Long id) {
        return repository.findByTeamOneOrTeamTwo(id, id);
    }

    public List<KickerGame> getAllGamesWithTeamIdInTeamOne(Long id) {
        return repository.findByTeamOne(id);
    }

    public List<KickerGame> getAllGamesWithTeamIdInTeamTwo(Long id) {
        return repository.findByTeamTwo(id);
    }
}
