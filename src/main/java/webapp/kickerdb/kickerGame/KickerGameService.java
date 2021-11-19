package webapp.kickerdb.kickerGame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeam;
import webapp.kickerdb.kickerTeam.KickerTeamService;
import webapp.kickerdb.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

@Service
public class KickerGameService {

    @Autowired
    private KickerPlayerService playerService;

    @Autowired
    private KickerTeamService teamService;

    @Autowired
    private KickerGameCommunicator gameCommunicator;

    public String addKickerGame(KickerGameRequest gameRequest) {
        if (new Utilities().hasDoublePlayer(gameRequest))
            return String.format("Game Set has double Players");
        if (!this.playerService.findPlayersWithGameRequest(gameRequest))
            return String.format("Game Set has unknown Player");
        KickerGame kickerGame = this.createGameOfGameRequest(gameRequest);
        Long id = this.gameCommunicator.getSaveGameId(kickerGame);
        return "Game ID " + id.toString() + "  saved";
    }

    private KickerGame createGameOfGameRequest(KickerGameRequest gameRequest){
        int winner = new Utilities().getWinner(gameRequest.getScoreOne(), gameRequest.getScoreTwo());
        KickerPlayer defensivePlayer = playerService.getPlayerByName(gameRequest.getPlayerTwo());
        KickerPlayer offensivePlayer = playerService.getPlayerByName(gameRequest.getPlayerTwo());
        Long teamOneId = teamService.getTeamIdWithDefenseAndOffensePlayerId(defensivePlayer.getId(), offensivePlayer.getId());
        defensivePlayer = playerService.getPlayerByName(gameRequest.getPlayerThree());
        offensivePlayer = playerService.getPlayerByName(gameRequest.getPlayerFour());
        Long teamTwoId = teamService.getTeamIdWithDefenseAndOffensePlayerId(defensivePlayer.getId(), offensivePlayer.getId());
        return new KickerGame(teamOneId, gameRequest.getScoreOne(), teamTwoId, gameRequest.getScoreTwo(), winner);
    }

    public int getCountOfAllGamesWithPlayerId(Long id) {
        int countGames = 0;
        List<KickerTeam> teams = teamService.getAllTeamsWithPlayerId(id);
        for (KickerTeam team:teams
        ) {
            List<KickerGame> games = gameCommunicator.getAllGamesWithTeamId(team.getId());
            countGames += games.size();
        }
        return countGames;
    }

    public List<KickerGame> getAllGamesWithPlayerIdInTeamOne(Long id) {
        List<KickerGame> games = new ArrayList<>();
        List<KickerTeam> teams = teamService.getAllTeamsWithPlayerId(id);
        for (KickerTeam team:teams
             ) {
            games = new Utilities().union(games, gameCommunicator.getAllGamesWithTeamIdInTeamOne(team.getId()));
        }
        return games;
    }

    public List<KickerGame> getAllGamesWithPlayerIdInTeamTwo(Long id) {
        List<KickerGame> games = new ArrayList<>();
        List<KickerTeam> teams = teamService.getAllTeamsWithPlayerId(id);
        for (KickerTeam team:teams
        ) {
            games = new Utilities().union(games, gameCommunicator.getAllGamesWithTeamIdInTeamTwo(team.getId()));
        }
        return games;
    }

    public int getCountOfAllGamesWithTeamId(Long id) {
        List<KickerGame> games = gameCommunicator.getAllGamesWithTeamId(id);
        return games.size();
    }
}
