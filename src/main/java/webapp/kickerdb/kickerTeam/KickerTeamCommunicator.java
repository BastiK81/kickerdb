package webapp.kickerdb.kickerTeam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KickerTeamCommunicator {

    @Autowired
    private KickerTeamRepository repository;


    public boolean hasTeamWithDefenseAndOffensePlayer(Long defensivePlayerId, Long offensivePlayerId) {
        return repository.findByPlayerDefensiveAndPlayerOffensive(defensivePlayerId, offensivePlayerId).isPresent();
    }

    public Long saveTeam(KickerTeam team) {
        return repository.save(team).getId();
    }

    public List<KickerTeam> getAllTeams() {
        return repository.findAll();
    }

    public Long getTeamIdWithDefensiveAndOffensivePlayerId(Long defensiveId, Long offensiveId) {
        return repository.findByPlayerDefensiveAndPlayerOffensive(defensiveId, offensiveId).get().getId();
    }

    public List<KickerTeam> getAllTeamsWithDefensiveOrOffensivePlayerId(Long idDefensive, Long idOffensive) {
        return repository.findByPlayerDefensiveOrPlayerOffensive(idDefensive, idOffensive);
    }
}
