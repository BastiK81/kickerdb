package webapp.kickerdb.kickerGame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeam;
import webapp.kickerdb.kickerTeam.KickerTeamService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KickerGameService {

    @Autowired
    private KickerPlayerService kickerPlayerService;

    @Autowired
    private KickerTeamService kickerTeamService;

    @Autowired
    private KickerGameRepository kickerGameRepository;

    public List<KickerRankingItem> getRanking() {
        List<webapp.kickerdb.kickerPlayer.KickerPlayer> allPlayers = kickerPlayerService.getAll();
        List<KickerRankingItem> ranking = new ArrayList<>();
        for (webapp.kickerdb.kickerPlayer.KickerPlayer player:allPlayers
             ) {
            ranking.add(new KickerRankingItem(player.getId(), player.getUserName(), 0,0,0,0));
        }
        for (KickerRankingItem item:ranking
             ) {
            List<KickerTeam> teams = kickerTeamService.findAllTeamsWithPlayer(item.getId());
            for (KickerTeam team:teams
                 ) {
                List<KickerGame> games = kickerGameRepository.findByTeamOne(team.getId());
                for (KickerGame game:games
                     ) {
                    item.setGames(item.getGames() + 1);
                    item.setScorePlus(item.getScorePlus() + game.getScoreTeamOne());
                    item.setScoreMinus(item.getScoreMinus() + game.getScoreTeamTwo());
                    if (game.getWinnerTeam() == 1)
                        item.setWins(item.getWins() + 3);
                    if (game.getWinnerTeam() == 0)
                        item.setWins(item.getWins() + 1);
                }
                games = kickerGameRepository.findByTeamTwo(team.getId());
                for (KickerGame game:games
                ) {
                    item.setGames(item.getGames() + 1);
                    item.setScorePlus(item.getScorePlus() + game.getScoreTeamTwo());
                    item.setScoreMinus(item.getScoreMinus() + game.getScoreTeamOne());
                    if (game.getWinnerTeam() == 2)
                        item.setWins(item.getWins() + 3);
                    if (game.getWinnerTeam() == 0)
                        item.setWins(item.getWins() + 1);
                }
            }
        }
        Collections.sort(ranking);
        return ranking;
    }

    public String getDoublePlayer(KickerGameRequest kickerGameRequest){
        List<String> players = new ArrayList<>();
        players.add(kickerGameRequest.getPlayerOne());
        if (players.contains(kickerGameRequest.getPlayerTwo()))
            return kickerGameRequest.getPlayerTwo();
        players.add(kickerGameRequest.getPlayerTwo());
        if (players.contains(kickerGameRequest.getPlayerThree()))
            return kickerGameRequest.getPlayerThree();
        players.add(kickerGameRequest.getPlayerThree());
        if (players.contains(kickerGameRequest.getPlayerFour()))
            return kickerGameRequest.getPlayerFour();
        players.add(kickerGameRequest.getPlayerFour());
        return "";
    }

    public String addKickerGame(KickerGameRequest kickerGameRequest) {
        String doublePlayer = getDoublePlayer(kickerGameRequest);
        if (!doublePlayer.isBlank())
            return String.format("Player %s is in the list more than once", doublePlayer);
        Long playerIdOne = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerOne());
        if (playerIdOne == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerOne());

        Long playerIdTwo = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerTwo());
        if (playerIdTwo == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerTwo());

        Long playerIdThree = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerThree());
        if (playerIdThree == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerThree());

        Long playerIdFour = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerFour());
        if (playerIdFour == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerFour());

        Long teamOne = kickerTeamService.getKickerTeamId(playerIdOne, playerIdTwo, 1);
        Long teamTwo = kickerTeamService.getKickerTeamId(playerIdThree, playerIdFour, 2);

        int winner = 0;
        if (kickerGameRequest.getScoreOne() > kickerGameRequest.getScoreTwo())
            winner = 1;
        if (kickerGameRequest.getScoreTwo() > kickerGameRequest.getScoreOne())
            winner = 2;

        KickerGame kickerGame = new KickerGame(teamOne, kickerGameRequest.getScoreOne(), teamTwo, kickerGameRequest.getScoreTwo(), winner);
        Long id = kickerGameRepository.save(kickerGame).getId();
        return "Game ID " + id.toString() + "  saved";
    }

    public int getGameCountAllGames(Long idOne, Long idTwo) {
        List<KickerGame> kickerGames = kickerGameRepository.findByTeamOneOrTeamTwo(idOne, idTwo);
        return kickerGames.size();
    }

    public int getPlayerPlayedGames(Long id) {
        int countGames = 0;
        List<KickerTeam> teams = kickerTeamService.findAllTeamsWithPlayer(id);
        for (KickerTeam team:teams
        ) {
            List<KickerGame> games = kickerGameRepository.findByTeamOne(team.getId());
            countGames += games.size();
            games = kickerGameRepository.findByTeamTwo(team.getId());
            countGames += games.size();
        }
        return countGames;
    }
}
