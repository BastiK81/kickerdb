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
    KickerPlayerService kickerPlayerService;

    @Autowired
    KickerTeamService kickerTeamService;

    @Autowired
    KickerGameService kickerGameService;

    public List<KickerPlayer> getTeamForecast() {
        List<KickerForecastTeam> allPossibleTemas = new ArrayList<>();
        List<KickerPlayer> players = kickerPlayerService.getAllActive();
        for (KickerPlayer defensivePlayer :players
            ) {
            for (KickerPlayer offensivePlayer:players
                ) {
                if (defensivePlayer.getId() == offensivePlayer.getId())
                    continue;
                KickerForecastTeam kickerForecastTeam = new KickerForecastTeam(defensivePlayer.getId(), offensivePlayer.getId());
                kickerForecastTeam.setIdOne(kickerTeamService.getKickerTeamId(defensivePlayer.getId(), offensivePlayer.getId(), 1));
                kickerForecastTeam.setIdTwo(kickerTeamService.getKickerTeamId(defensivePlayer.getId(), offensivePlayer.getId(), 2));
                kickerForecastTeam.setGames(kickerGameService.getGameCountAllGames(kickerForecastTeam.getIdOne(),kickerForecastTeam.getIdTwo()));
                int singleGames = kickerGameService.getPlayerPlayedGames(kickerForecastTeam.getPlayerDefensiveId());
                singleGames += kickerGameService.getPlayerPlayedGames(kickerForecastTeam.getPlayerOffensiveId());
                kickerForecastTeam.setSingleGames(singleGames);
                allPossibleTemas.add(kickerForecastTeam);
            }
        }
        int mark = 5;
        Collections.sort(allPossibleTemas);
        List<KickerPlayer> forecast = new ArrayList<>();
        for (int i = 0; i < allPossibleTemas.size(); i++) {
            for (int j = 0; j < allPossibleTemas.size(); j++) {
                String defensiveOne = kickerPlayerService.getPlayerNameByID(allPossibleTemas.get(i).getPlayerDefensiveId());
                String offensiveOne = kickerPlayerService.getPlayerNameByID(allPossibleTemas.get(i).getPlayerOffensiveId());
                String defensiveTwo = kickerPlayerService.getPlayerNameByID(allPossibleTemas.get(j).getPlayerDefensiveId());
                String offensiveTwo = kickerPlayerService.getPlayerNameByID(allPossibleTemas.get(j).getPlayerOffensiveId());
                KickerGameRequest kickerGameRequest = new KickerGameRequest(defensiveOne,offensiveOne,defensiveTwo,offensiveTwo);
                String doublePlayer = kickerGameService.getDoublePlayer(kickerGameRequest);
                if (doublePlayer.isBlank()) {
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTemas.get(i).getPlayerDefensiveId()));
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTemas.get(i).getPlayerOffensiveId()));
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTemas.get(j).getPlayerDefensiveId()));
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTemas.get(j).getPlayerOffensiveId()));
                    return forecast;
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
        return forecast;
    }

    public List<KickerPlayer> getPlayerForecast() {
        List<KickerPlayer> players = kickerPlayerService.getAllActive();
        List<KickerForecastPlayerItem> kickerForecastPlayerItems = new ArrayList<>();
        List<Long> playerIds = new ArrayList<>();
        for (KickerPlayer player:players
            ) {
            KickerForecastPlayerItem playerItem = new KickerForecastPlayerItem(player.getId(), player.getUserName());
            playerItem.setGames(kickerGameService.getPlayerPlayedGames(player.getId()));
            kickerForecastPlayerItems.add(playerItem);
        }
        Collections.sort(kickerForecastPlayerItems);
        List<KickerTeam> teamsTemp = null;
        List<KickerTeam> teamsAdd;
        int count = 1;
        for (KickerForecastPlayerItem item:kickerForecastPlayerItems
        ) {
            if (count < 2) {
                playerIds.add(item.getId());
                teamsTemp = kickerTeamService.findAllTeamsWithPlayer(item.getId());
                count++;
            } else if (count < 5) {
                playerIds.add(item.getId());
                teamsAdd = kickerTeamService.findAllTeamsWithPlayer(item.getId());
                teamsTemp = union(teamsTemp, teamsAdd);
                count++;
            }
        }

        List<KickerTeam> teams = new ArrayList<>();
        for (KickerTeam team:teamsTemp
            ) {
            if (playerIds.contains(team.getPlayerDefensive()))
                if (playerIds.contains(team.getPlayerOffensive()))
                    teams.add(team);
        }
        List<KickerForecastTeam> allPossibleTeams = new ArrayList<>();
        for (KickerTeam team:teams
            ) {
            KickerForecastTeam kickerForecastTeam = new KickerForecastTeam(team.getPlayerDefensive(), team.getPlayerOffensive());
            kickerForecastTeam.setIdOne(kickerTeamService.getKickerTeamId(team.getPlayerDefensive(), team.getPlayerOffensive(), 1));
            kickerForecastTeam.setIdTwo(kickerTeamService.getKickerTeamId(team.getPlayerDefensive(), team.getPlayerOffensive(), 2));
            kickerForecastTeam.setGames(kickerGameService.getGameCountAllGames(kickerForecastTeam.getIdOne(),kickerForecastTeam.getIdTwo()));
            int singleGames = kickerGameService.getPlayerPlayedGames(kickerForecastTeam.getPlayerDefensiveId());
            singleGames += kickerGameService.getPlayerPlayedGames(kickerForecastTeam.getPlayerOffensiveId());
            kickerForecastTeam.setSingleGames(singleGames);
            allPossibleTeams.add(kickerForecastTeam);
        }
        Collections.sort(allPossibleTeams);
        int mark = 5;
        List<KickerPlayer> forecast = new ArrayList<>();
        for (int i = 0; i < allPossibleTeams.size(); i++) {
            for (int j = 0; j < allPossibleTeams.size(); j++) {
                String defensiveOne = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(i).getPlayerDefensiveId());
                String offensiveOne = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(i).getPlayerOffensiveId());
                String defensiveTwo = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(j).getPlayerDefensiveId());
                String offensiveTwo = kickerPlayerService.getPlayerNameByID(allPossibleTeams.get(j).getPlayerOffensiveId());
                KickerGameRequest kickerGameRequest = new KickerGameRequest(defensiveOne,offensiveOne,defensiveTwo,offensiveTwo);
                String doublePlayer = kickerGameService.getDoublePlayer(kickerGameRequest);
                if (doublePlayer.isBlank()) {
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(i).getPlayerDefensiveId()));
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(i).getPlayerOffensiveId()));
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(j).getPlayerDefensiveId()));
                    forecast.add(kickerPlayerService.getPlayerByID(allPossibleTeams.get(j).getPlayerOffensiveId()));
                    return forecast;
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
        return forecast;
    }

    private <KickerTeam> List<KickerTeam> intersection(List<KickerTeam> list1, List<KickerTeam> list2) {
        List<KickerTeam> list = new ArrayList<KickerTeam>();
        for (KickerTeam t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
    private <KickerTeam> List<KickerTeam> union(List<KickerTeam> list1, List<KickerTeam> list2) {
        Set<KickerTeam> set = new HashSet<KickerTeam>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<KickerTeam>(set);
    }
}
