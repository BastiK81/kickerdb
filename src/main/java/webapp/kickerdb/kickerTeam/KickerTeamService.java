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
    private KickerTeamCommunicator teamCommunicator;

    public void addAllPossibleTeamsWithPlayerList(List<KickerPlayer> allPlayers) {
        for (int i = 0; i < allPlayers.size(); i++) {
            for (int j = 0; j < allPlayers.size(); j++) {
                if (i == j)
                    continue;
                Long defensivePlayerId = allPlayers.get(i).getId();
                Long offensivePlayerId = allPlayers.get(j).getId();
                if (this.teamCommunicator.hasTeamWithDefenseAndOffensePlayer(defensivePlayerId, offensivePlayerId))
                    this.teamCommunicator.saveTeam(new KickerTeam(defensivePlayerId, offensivePlayerId));
            }
        }
        List<KickerTeam> allPossibleTeams = this.teamCommunicator.getAllTeams();
        this.forecastService.addAllPossibleGames(allPossibleTeams);
    }

    public Long getTeamIdWithDefenseAndOffensePlayerId(Long defensiveId, Long offensiveId) {
        if (this.teamCommunicator.hasTeamWithDefenseAndOffensePlayer(defensiveId,offensiveId))
            return this.teamCommunicator.getTeamIdWithDefensiveAndOffensivePlayerId(defensiveId, offensiveId);
        return this.teamCommunicator.saveTeam(new KickerTeam(defensiveId, offensiveId));
    }

    public List<KickerTeam> getAllTeamsWithPlayerId(Long id) {
        return this.teamCommunicator.getAllTeamsWithDefensiveOrOffensivePlayerId(id, id);
    }

    public List<KickerTeam> getAllTeams() {
        return teamCommunicator.getAllTeams();
    }
}
