package webapp.kickerdb.kickerTeam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerforecast.KickerForecastService;

import java.util.List;

@Service
public class KickerTeamService {

    @Autowired
    private KickerForecastService forecastService;

    @Autowired
    private KickerTeamCommunicator communicator;

    public void addAllPossibleTeamsWithPlayerList(List<KickerPlayer> allPlayers) {
        for (int i = 0; i < allPlayers.size(); i++) {
            for (int j = 0; j < allPlayers.size(); j++) {
                if (i == j)
                    continue;
                Long defensivePlayerId = allPlayers.get(i).getId();
                Long offensivePlayerId = allPlayers.get(j).getId();
                if (!this.communicator.hasTeamWithDefenseAndOffensePlayer(defensivePlayerId, offensivePlayerId))
                    this.communicator.saveTeam(new KickerTeam(defensivePlayerId, offensivePlayerId));
            }
        }
    }

    public Long getTeamIdWithDefenseAndOffensePlayerId(Long defensiveId, Long offensiveId) {
        if (this.communicator.hasTeamWithDefenseAndOffensePlayer(defensiveId,offensiveId))
            return this.communicator.getTeamIdWithDefensiveAndOffensivePlayerId(defensiveId, offensiveId);
        return this.communicator.saveTeam(new KickerTeam(defensiveId, offensiveId));
    }

    public List<KickerTeam> getAllTeamsWithPlayerId(Long id) {
        return this.communicator.getAllTeamsWithDefensiveOrOffensivePlayerId(id, id);
    }

    public List<KickerTeam> getAllTeams() {
        return communicator.getAllTeams();
    }
}
