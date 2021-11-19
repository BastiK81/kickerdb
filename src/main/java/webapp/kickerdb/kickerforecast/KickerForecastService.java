package webapp.kickerdb.kickerforecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerGame.KickerGameRequest;
import webapp.kickerdb.kickerGame.KickerGameService;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeam;
import webapp.kickerdb.kickerTeam.KickerTeamService;

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

    @Autowired
    private KickerForecastRepository forecastRepository;

    public List<KickerPlayer> getTeamForecast() {
        List<Long> allActivPlayerIds = this.getAllActivePlayerIds();
        List<KickerForecastTeamItem> teamItems = getTeamItems();
        List<KickerPlayer> forecast = new ArrayList<>();
        for (KickerForecastTeamItem team : teamItems
        ) {
            if (allActivPlayerIds.contains(team.getPlayerOffensiveId()) && allActivPlayerIds.contains(team.getPlayerDefensiveId())) {
                forecast.add(playerService.getPlayerById(team.getPlayerDefensiveId()));
                forecast.add(playerService.getPlayerById(team.getPlayerOffensiveId()));
                allActivPlayerIds.remove(team.getPlayerDefensiveId());
                allActivPlayerIds.remove(team.getPlayerOffensiveId());
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
            int gamesCounter = this.communicator.getGameCountWithTeamOneIdAndTeamTwoId(game.getTeamOneId(), game.getTeamTwoId());
            game.setGames(gamesCounter);
            gamesCounter = this.communicator.
        }
        return
    }
}
