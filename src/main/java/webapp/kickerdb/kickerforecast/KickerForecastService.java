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

    @Autowired
    KickerForecastCrawler kickerForecastCrawler;

    @Autowired
    private KickerForecastRepository forecastRepository;

    public List<KickerPlayer> getTeamForecast() {
        List<KickerForecastTeamItem> allPossibleTeams = kickerForecastCrawler.getAllPossibleTeams();
        return kickerForecastCrawler.getForecast(allPossibleTeams);
    }

    public List<KickerPlayer> getPlayerForecast() {
        List<KickerPlayer> players = kickerPlayerService.getAllActive();
        List<KickerForecastPlayerItem> kickerForecastPlayerItems = new ArrayList<>();
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
        List<Long> playerIds = new ArrayList<>();
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
        List<KickerForecastTeamItem> allPossibleTeams = new ArrayList<>();
        for (KickerTeam team:teams
            ) {
            KickerForecastTeamItem kickerForecastTeamItem = new KickerForecastTeamItem(team.getPlayerDefensive(), team.getPlayerOffensive());
            kickerForecastTeamItem.setIdOne(kickerTeamService.getKickerTeamId(team.getPlayerDefensive(), team.getPlayerOffensive()));
            kickerForecastTeamItem.setIdTwo(kickerTeamService.getKickerTeamId(team.getPlayerDefensive(), team.getPlayerOffensive()));
            kickerForecastTeamItem.setGames(kickerGameService.getGameCountAllGames(kickerForecastTeamItem.getIdOne(), kickerForecastTeamItem.getIdTwo()));
            int singleGames = kickerGameService.getPlayerPlayedGames(kickerForecastTeamItem.getPlayerDefensiveId());
            singleGames += kickerGameService.getPlayerPlayedGames(kickerForecastTeamItem.getPlayerOffensiveId());
            kickerForecastTeamItem.setSingleGames(singleGames);
            allPossibleTeams.add(kickerForecastTeamItem);
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

    private <KickerTeam> List<KickerTeam> union(List<KickerTeam> list1, List<KickerTeam> list2) {
        Set<KickerTeam> set = new HashSet<KickerTeam>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<KickerTeam>(set);
    }

    public List<KickerPlayer> getGameForecast() {
        List<KickerForecastGameItem> allPossibleGames = getAllPossibleGames();
        return setForecastOfAllPossibleGames(allPossibleGames);
    }

    private List<KickerForecastGameItem> getAllPossibleGames(){
        List<KickerPlayer> players = kickerPlayerService.getAllActive();
        List<KickerForecastGameItem> allPossibleGames = new ArrayList<>();
        for (KickerPlayer playerOne:players
            ) {
            List<Long> playerIds = new ArrayList<>();
            playerIds.add(playerOne.getId());
            for (KickerPlayer playerTwo:players
            ) {
                if (playerIds.contains(playerTwo.getId()))
                    continue;
                playerIds.add(playerTwo.getId());
                for (KickerPlayer playerThree:players
                ) {
                    if (playerIds.contains(playerThree.getId()))
                        continue;
                    playerIds.add(playerThree.getId());
                    for (KickerPlayer playerFour:players
                    ) {
                        if (playerIds.contains(playerFour.getId()))
                            continue;
                        playerIds.add(playerFour.getId());
                        Long teamOneId = kickerTeamService.getKickerTeamId(playerOne.getId(), playerTwo.getId());
                        Long teamTwoId = kickerTeamService.getKickerTeamId(playerThree.getId(), playerFour.getId());
                        int games = kickerGameService.getGamePlayedGames(teamOneId, teamTwoId);
                        int teamGames = this.getAllTeamsGames(playerIds);
                        int playerGames = this.getPlayerGames(playerIds);
                        allPossibleGames.add(new KickerForecastGameItem(teamOneId, playerOne.getId(), playerTwo.getId(), teamTwoId, playerThree.getId(), playerFour.getId(), games, teamGames, playerGames));
                    }
                }
            }
        }
        Collections.sort(allPossibleGames);
        return allPossibleGames;
    }

    private int getAllTeamsGames(List<Long> playerIds) {
        List<KickerTeam> teams = kickerTeamService.getAllTeamsWithPlayerId(playerIds.get(0), playerIds.get(1));
        teams = union(teams, kickerTeamService.getAllTeamsWithPlayerId(playerIds.get(2), playerIds.get(3)));
        int games = 0;
        for (KickerTeam team:teams
            ) {
            games += kickerGameService.getGameCountAllGames(team.getId(), team.getId());
        }
        return games;
    }

    private int getPlayerGames(List<Long> playerIds){
        int countGames = 0;
        for (Long playerId:playerIds
            ) {
            countGames += kickerGameService.getPlayerPlayedGames(playerId);
        }
        return countGames;
    }

    private List<KickerPlayer> setForecastOfAllPossibleGames(List<KickerForecastGameItem> allPossibleGames) {
        List<KickerPlayer> forecast = new ArrayList<>();
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleGames.get(0).getTeamOneDefensiveId()));
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleGames.get(0).getTeamOneOffensiveId()));
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleGames.get(0).getTeamTwoDefensiveId()));
        forecast.add(kickerPlayerService.getPlayerByID(allPossibleGames.get(0).getTeamTwoOffensiveId()));
        return forecast;
    }

    public void addAllPossibleGames(List<KickerTeam> allPossibleTeams) {
        for (int i = 0; i < allPossibleTeams.size(); i++) {
            for (int j = 0; j < allPossibleTeams.size(); j++) {
                if (i == j)
                    continue;
                KickerTeam teamOne = allPossibleTeams.get(i);
                KickerTeam teamTwo = allPossibleTeams.get(j);
                List<Long> playerIds = new ArrayList<>();
                playerIds.add(teamOne.getPlayerDefensive());
                playerIds.add(teamOne.getPlayerOffensive());
                if (playerIds.contains(teamTwo.getPlayerDefensive()))
                    continue;
                if (playerIds.contains(teamTwo.getPlayerOffensive()))
                    continue;
                if (!this.forecastRepository.findByTeamOneIdAndTeamTwoId(teamOne.getId(), teamTwo.getId()).isEmpty())
                    continue;
                KickerForecastGameItem item = new KickerForecastGameItem(teamOne.getId(), teamOne.getPlayerDefensive(), teamOne.getPlayerOffensive(), teamTwo.getId(), teamTwo.getPlayerDefensive(), teamTwo.getPlayerOffensive());
                this.forecastRepository.save(item);
            }
        }
    }
}
