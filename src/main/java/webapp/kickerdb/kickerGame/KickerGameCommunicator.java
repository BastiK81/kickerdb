package webapp.kickerdb.kickerGame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;

@Component
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

    public List<KickerGame> getAllGamesWithTeamIdInTeamOneAndTeamIdInTeamTwo(Long teamOneId, Long teamTwoId) {
        return this.repository.findByTeamOneAndTeamTwo(teamOneId, teamTwoId);
    }

    public Integer countAllGames() {
        return repository.findAll().size();
    }
}
