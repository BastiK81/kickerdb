package webapp.kickerdb.kickerforecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import webapp.kickerdb.kickerGame.KickerGameRequest;
import webapp.kickerdb.kickerGame.KickerGameService;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeamService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KickerForecastCrawler {

    @Autowired
    KickerPlayerService kickerPlayerService;

    @Autowired
    KickerTeamService kickerTeamService;

    @Autowired
    KickerGameService kickerGameService;

    private List<KickerForecastPlayerItem> getKickerForecastPlayerItems(){
        List<KickerPlayer> players = kickerPlayerService.getAllActive();
        List<KickerForecastPlayerItem> kickerForecastPlayerItems = new ArrayList<>();
        for (KickerPlayer player:players
        ) {
            KickerForecastPlayerItem playerItem = new KickerForecastPlayerItem(player.getId(), player.getUserName());
            playerItem.setGames(kickerGameService.getPlayerPlayedGames(player.getId()));
            kickerForecastPlayerItems.add(playerItem);
        }
        Collections.sort(kickerForecastPlayerItems);
        return kickerForecastPlayerItems;
    }


    public List<KickerForecastTeamItem> getAllPossibleTeams() {
        List<KickerForecastTeamItem> allPossibleTeams = new ArrayList<>();
        List<KickerPlayer> players = kickerPlayerService.getAllActive();
        for (KickerPlayer defensivePlayer :players
        ) {
            for (KickerPlayer offensivePlayer:players
            ) {
                if (defensivePlayer.getId() == offensivePlayer.getId())
                    continue;
                allPossibleTeams.add(getForecastTeam(defensivePlayer.getId(), offensivePlayer.getId()));
            }
        }
        Collections.sort(allPossibleTeams);
        return allPossibleTeams;
    }

    private KickerForecastTeamItem getForecastTeam(Long defensiveId, Long offensiveId){
        KickerForecastTeamItem kickerForecastTeamItem = new KickerForecastTeamItem(defensiveId, offensiveId);
        kickerForecastTeamItem.setIdOne(kickerTeamService.getKickerTeamId(defensiveId, offensiveId));
        kickerForecastTeamItem.setIdTwo(kickerTeamService.getKickerTeamId(defensiveId, offensiveId));
        kickerForecastTeamItem.setGames(kickerGameService.getGameCountAllGames(kickerForecastTeamItem.getIdOne(), kickerForecastTeamItem.getIdTwo()));
        int singleGames = kickerGameService.getPlayerPlayedGames(kickerForecastTeamItem.getPlayerDefensiveId());
        singleGames += kickerGameService.getPlayerPlayedGames(kickerForecastTeamItem.getPlayerOffensiveId());
        kickerForecastTeamItem.setSingleGames(singleGames);
        return kickerForecastTeamItem;
    }

    public List<KickerPlayer> getForecast(List<KickerForecastTeamItem> allPossibleTeams){
        int mark = 5;
        for (int i = 0; i < allPossibleTeams.size(); i++) {
            for (int j = 0; j < allPossibleTeams.size(); j++) {
                String doublePlayer = kickerGameService.getDoublePlayer(getGameRequest(allPossibleTeams, i, j));
                if (doublePlayer.isBlank()) {
                    return getForecastData(allPossibleTeams, i, j);
                }
                if (j == mark) {
                    i += 1;
                    j = 0;
                    if (i == mark) {
                        i = 0;
                        j = 0;
                        mark += 5;
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private KickerGameRequest getGameRequest(List<KickerForecastTeamItem> allPossibleTeams, int i, int j){
        String defensiveOne = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(i).getPlayerDefensiveId());
        String offensiveOne = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(i).getPlayerOffensiveId());
        String defensiveTwo = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(j).getPlayerDefensiveId());
        String offensiveTwo = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(j).getPlayerOffensiveId());
        return new KickerGameRequest(defensiveOne,offensiveOne,defensiveTwo,offensiveTwo);
    }

    private List<KickerPlayer> getForecastData(List<KickerForecastTeamItem> allPossibleTeams, int i, int j){
        List<KickerPlayer> forecast = new ArrayList<>();
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(i).getPlayerDefensiveId()));
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(i).getPlayerOffensiveId()));
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(j).getPlayerDefensiveId()));
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(j).getPlayerOffensiveId()));
        return forecast;
    }
}
