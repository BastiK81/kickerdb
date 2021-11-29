package webapp.kickerdb.kickerforecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerGame.KickerGameService;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeam;
import webapp.kickerdb.kickerTeam.KickerTeamService;
import webapp.kickerdb.utilities.Utilities;

import java.util.*;

@Service
public class KickerForecastService {

    @Autowired
    KickerPlayerService playerService;

    @Autowired
    KickerTeamService teamService;

    @Autowired
    KickerGameService gameService;

    @Autowired
    KickerForecastCommunicator communicator;

    public List<KickerPlayer> getTeamForecast() {
        List<Long> allActivePlayerIds = this.getAllActivePlayerIds();
        List<KickerForecastTeamItem> teamItems = getTeamItems();
        List<KickerPlayer> forecast = new ArrayList<>();
        for (KickerForecastTeamItem team : teamItems
        ) {
            if (allActivePlayerIds.contains(team.getPlayerOffensiveId()) && allActivePlayerIds.contains(team.getPlayerDefensiveId())) {
                forecast.add(playerService.getPlayerById(team.getPlayerDefensiveId()));
                forecast.add(playerService.getPlayerById(team.getPlayerOffensiveId()));
                allActivePlayerIds.remove(team.getPlayerDefensiveId());
                allActivePlayerIds.remove(team.getPlayerOffensiveId());
            }
            if (forecast.size() == 4)
                break;
        }
        return forecast;
    }

    private List<Long> getAllActivePlayerIds(){
        List<Long> allActivePlayerIds = new ArrayList<>();
        List<KickerPlayer> allActivePlayer = playerService.getAllActivePlayer();
        for (KickerPlayer player:allActivePlayer
             ) {
            allActivePlayerIds.add(player.getId());
        }
        return allActivePlayerIds;
    }

    public List<KickerPlayer> getPlayerForecast() {
        List<KickerForecastPlayerItem> playerItems = getPlayerItems();
        List<KickerForecastTeamItem> teamItems = getTeamItems();
        List<Long> lastFourPlayerIds = getLastFourPlayerIds(playerItems);
        List<KickerPlayer> forecast = new ArrayList<>();
        for (KickerForecastTeamItem team : teamItems
        ) {
            if (lastFourPlayerIds.contains(team.getPlayerOffensiveId()) && lastFourPlayerIds.contains(team.getPlayerDefensiveId())) {
                forecast.add(playerService.getPlayerById(team.getPlayerDefensiveId()));
                forecast.add(playerService.getPlayerById(team.getPlayerOffensiveId()));
                lastFourPlayerIds.remove(team.getPlayerDefensiveId());
                lastFourPlayerIds.remove(team.getPlayerOffensiveId());
            }
        }
        return forecast;
    }

    private List<KickerForecastPlayerItem> getPlayerItems(){
        List<KickerPlayer> players = playerService.getAllActivePlayer();
        List<KickerForecastPlayerItem> playerItems = new ArrayList<>();
        for (KickerPlayer player:players
        ) {
            KickerForecastPlayerItem playerItem = new KickerForecastPlayerItem(player.getId(), player.getUserName());
            int gameCount = gameService.getCountOfAllGamesWithPlayerId(player.getId());
            playerItem.setGames(gameCount);
            playerItems.add(playerItem);
        }
        Collections.sort(playerItems);
        return playerItems;
    }

    private List<KickerForecastTeamItem> getTeamItems() {
        List<KickerTeam> teams = teamService.getAllTeams();
        List<KickerForecastTeamItem> teamItems = new ArrayList<>();
        for (KickerTeam team:teams
        ) {
            KickerForecastTeamItem teamItem = new KickerForecastTeamItem(team.getPlayerDefensive(), team.getPlayerOffensive());
            int gameCount = gameService.getCountOfAllGamesWithTeamId(team.getId());
            teamItem.setGames(gameCount);
            gameCount = gameService.getCountOfAllGamesWithPlayerId(team.getPlayerDefensive()) + gameService.getCountOfAllGamesWithPlayerId(team.getPlayerOffensive());
            teamItem.setSingleGames(gameCount);
            teamItems.add(teamItem);
        }
        Collections.sort(teamItems);
        return teamItems;
    }

    private List<Long> getLastFourPlayerIds(List<KickerForecastPlayerItem> playerItems ){
        List<Long> lastFourPlayerIds = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            lastFourPlayerIds.add(playerItems.get(i).getId());
        }
        return lastFourPlayerIds;
    }

    public List<KickerPlayer> getGameForecast() {
        List<KickerForecastGameItem> allPossibleGames = this.communicator.getAllPossibleGames();
        for (KickerForecastGameItem game:allPossibleGames
             ) {
            int gamesCounter = this.getCountAllGamesWithFixTeams(game.getTeamOneId(), game.getTeamTwoId());
            game.setGames(gamesCounter);
            gamesCounter += this.getCountPlayerConstellation(game);
            game.setVsGames(gamesCounter);
            game.setTeamOneGames(this.gameService.getCountOfAllGamesWithTeamId(game.getTeamOneId()));
            game.setTeamTwoGames(this.gameService.getCountOfAllGamesWithTeamId(game.getTeamTwoId()));
            game.setTeamGames(game.getTeamOneGames() + game.getTeamTwoGames());
            gamesCounter = this.getCountAllTeamConstGames(game);
            game.setTeamConstGames(gamesCounter);
            gamesCounter = this.gameService.getCountOfAllGamesWithPlayerId(game.getTeamOneDefensiveId());
            gamesCounter += this.gameService.getCountOfAllGamesWithPlayerId(game.getTeamOneOffensiveId());
            gamesCounter += this.gameService.getCountOfAllGamesWithPlayerId(game.getTeamTwoDefensiveId());
            gamesCounter += this.gameService.getCountOfAllGamesWithPlayerId(game.getTeamTwoOffensiveId());
            game.setPlayerGames(gamesCounter);
        }
        Collections.sort(allPossibleGames);
        List<KickerPlayer> forecast = new ArrayList<>();
        for (KickerForecastGameItem game:allPossibleGames
             ) {
            if (this.allPlayersActive(game)) {
                forecast.add(this.playerService.getPlayerById(game.getTeamOneDefensiveId()));
                forecast.add(this.playerService.getPlayerById(game.getTeamOneOffensiveId()));
                forecast.add(this.playerService.getPlayerById(game.getTeamTwoDefensiveId()));
                forecast.add(this.playerService.getPlayerById(game.getTeamTwoOffensiveId()));
                return forecast;
            }
        }
        return forecast;
    }

    private int getCountAllTeamConstGames(KickerForecastGameItem game) {
        Long switchedTeamOneId = this.teamService.getTeamIdWithDefenseAndOffensePlayerId(game.getTeamOneOffensiveId(), game.getTeamOneDefensiveId());
        Long switchedTeamTwoId = this.teamService.getTeamIdWithDefenseAndOffensePlayerId(game.getTeamTwoOffensiveId(), game.getTeamTwoDefensiveId());
        int gameCounter = this.gameService.getCountOfAllGamesWithTeamId(switchedTeamOneId);
        gameCounter += this.gameService.getCountOfAllGamesWithTeamId(switchedTeamTwoId);
        return gameCounter;
    }

    private boolean allPlayersActive(KickerForecastGameItem game) {
        return this.playerService.getPlayerById(game.getTeamOneDefensiveId()).isActive() &&
                this.playerService.getPlayerById(game.getTeamOneOffensiveId()).isActive() &&
                this.playerService.getPlayerById(game.getTeamTwoDefensiveId()).isActive() &&
                this.playerService.getPlayerById(game.getTeamTwoOffensiveId()).isActive();
    }

    private int getCountPlayerConstellation(KickerForecastGameItem game) {
        Long switchedTeamOneId = this.teamService.getTeamIdWithDefenseAndOffensePlayerId(game.getTeamOneOffensiveId(), game.getTeamOneDefensiveId());
        int gameCounter = this.getCountAllGamesWithFixTeams(game.getTeamTwoId(), switchedTeamOneId);
        Long switchedTeamTwoId = this.teamService.getTeamIdWithDefenseAndOffensePlayerId(game.getTeamTwoOffensiveId(), game.getTeamTwoDefensiveId());
        gameCounter += this.getCountAllGamesWithFixTeams(game.getTeamOneId(), switchedTeamTwoId);
        gameCounter += this.getCountAllGamesWithFixTeams(switchedTeamOneId, switchedTeamTwoId);
        return gameCounter;
    }

    private int getCountAllGamesWithFixTeams(Long idOne, Long idTwo) {
        int gamesCounter = this.gameService.getCountOfAllGamesWithTeamOneAndTeamTwo(idOne, idTwo);
        gamesCounter += this.gameService.getCountOfAllGamesWithTeamOneAndTeamTwo(idTwo, idOne);
        return gamesCounter;
    }

    public void addAllPossibleGames(List<KickerTeam> allPossibleTeams) {
        for (int i = 0; i < allPossibleTeams.size(); i++) {
            for (int j = 0; j < allPossibleTeams.size(); j++) {
                if (i == j)
                    continue;
                KickerTeam teamOne = allPossibleTeams.get(i);
                KickerTeam teamTwo = allPossibleTeams.get(j);
                if (new Utilities().hasTeamsDoublePlayer(teamOne, teamTwo))
                    continue;
                if (!this.hasGameInForecast(teamOne.getId(), teamTwo.getId()))
                    continue;
                KickerForecastGameItem game = setForecastGame(teamOne, teamTwo);
                communicator.saveKickerforecastGameItem(game);
            }
        }
    }

    private KickerForecastGameItem setForecastGame(KickerTeam teamOne, KickerTeam teamTwo) {
        KickerForecastGameItem game = new KickerForecastGameItem(
                                                            teamOne.getId(),
                                                            teamOne.getPlayerDefensive(),
                                                            teamOne.getPlayerOffensive(),
                                                            teamTwo.getId(),
                                                            teamTwo.getPlayerDefensive(),
                                                            teamTwo.getPlayerOffensive()
                                                            );
        return game;
    }


    private boolean hasGameInForecast(Long idTeamOne, Long idTeamTwo) {
        boolean checkOne = communicator.hasGameWithTeamIdOneAndTeamIdTwo(idTeamOne, idTeamTwo);
        boolean checkTwo = communicator.hasGameWithTeamIdOneAndTeamIdTwo(idTeamTwo, idTeamOne);
        return checkOne && checkTwo;
    }

}