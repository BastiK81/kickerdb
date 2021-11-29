package webapp.kickerdb.kickerPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerGame.KickerGameRequest;
import webapp.kickerdb.kickerTeam.KickerTeam;
import webapp.kickerdb.kickerTeam.KickerTeamService;
import webapp.kickerdb.kickerforecast.KickerForecastService;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class KickerPlayerService {

    @Autowired
    private KickerTeamService teamService;

    @Autowired
    private KickerForecastService forecastService;

    @Autowired
    private KickerPlayerCommunicator playerCommunicator;

    private static final String PLAYER_NOT_FOUND = "Player %s not found";

    public String addKickerPlayer(KickerPlayer player) {
        if (this.playerCommunicator.hasPlayerWithName(player.getUserName()))
            return String.format("Player %s already exist", player.getUserName());
        player.setActive(true);
        Long id = this.playerCommunicator.savePlayer(player);
        if (!this.playerCommunicator.hasPlayerWithId(id))
            return String.format("Player %s could not be saved", player.getUserName());
        List<KickerPlayer> allPlayers = this.playerCommunicator.getAllPlayer();
        this.teamService.addAllPossibleTeamsWithPlayerList(allPlayers);
        List<KickerTeam> allPossibleTeams = this.teamService.getAllTeams();
        this.forecastService.addAllPossibleGames(allPossibleTeams);
        return String.format("Player %s added with id %s", player.getUserName(), id);
    }

    public String deletePlayerByName(String name) {
        if (!this.playerCommunicator.hasPlayerWithName(name))
            return String.format(PLAYER_NOT_FOUND, name);
        Long id = this.playerCommunicator.getPlayerIdByName(name);
        this.playerCommunicator.deletePlayerById(id);
        return String.format("Player %s deleted", name);
    }

    @Transactional
    public String changePlayerActivityByName(String name) {
        if (!this.playerCommunicator.hasPlayerWithName(name))
            return String.format(PLAYER_NOT_FOUND, name);
        Long id = this.playerCommunicator.getPlayerIdByName(name);
        KickerPlayer player = this.playerCommunicator.getPlayerById(id);
        player.setActive(!player.isActive());
        return String.format("Activity for Player %s changed", name);
    }

    public List<KickerPlayer> getAllActivePlayer() {
        return this.playerCommunicator.getAllActivePlayer();
    }

    public List<KickerPlayer> getAllPlayer() {
        return this.playerCommunicator.getAllPlayer();
    }

    public boolean findPlayersWithGameRequest(KickerGameRequest gameRequest) {
        if (!this.playerCommunicator.hasPlayerWithName(gameRequest.getPlayerOne()))
            return false;
        if (!this.playerCommunicator.hasPlayerWithName(gameRequest.getPlayerTwo()))
            return false;
        if (!this.playerCommunicator.hasPlayerWithName(gameRequest.getPlayerThree()))
            return false;
        if (!this.playerCommunicator.hasPlayerWithName(gameRequest.getPlayerFour()))
            return false;
        return true;
    }

    public KickerPlayer getPlayerByName(String name) {
        return playerCommunicator.getPlayerByName(name);
    }

    public KickerPlayer getPlayerById(Long id) {
        return this.playerCommunicator.getPlayerById(id);
    }

    public boolean getActivity(Long id) {
        return this.playerCommunicator.getPlayerActivity(id);
    }
}
