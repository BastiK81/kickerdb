package webapp.kickerdb.kickerRanking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerGame.KickerGame;
import webapp.kickerdb.kickerGame.KickerGameService;
import webapp.kickerdb.kickerPlayer.KickerPlayer;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeamService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KickerRankingService {

    @Autowired
    private KickerPlayerService playerService;

    @Autowired
    private KickerTeamService teamService;

    @Autowired
    private KickerGameService gameService;

    public List<KickerRankingItem> getRanking() {
        List<KickerRankingItem> ranking = new ArrayList<>();
        for (KickerPlayer player:playerService.getAllPlayer()
        ) {
            KickerRankingItem rankingItem = new KickerRankingItem(player.getId(), player.getUserName(), 0,0,0F,0,0F,0, false);
            for (KickerGame game:gameService.getAllGamesWithPlayerIdInTeamOne(player.getId())
            ) {
                rankingItem.setGames(rankingItem.getGames() + 1);
                rankingItem.setScorePlus(rankingItem.getScorePlus() + game.getScoreTeamOne());
                rankingItem.setScoreMinus(rankingItem.getScoreMinus() + game.getScoreTeamTwo());
                    if (game.getWinnerTeam() == 1)
                        rankingItem.setWins(rankingItem.getWins() + 3);
                    if (game.getWinnerTeam() == 0)
                        rankingItem.setWins(rankingItem.getWins() + 1);
            }
            for (KickerGame game:gameService.getAllGamesWithPlayerIdInTeamTwo(player.getId())
            ) {
                rankingItem.setGames(rankingItem.getGames() + 1);
                rankingItem.setScorePlus(rankingItem.getScorePlus() + game.getScoreTeamTwo());
                rankingItem.setScoreMinus(rankingItem.getScoreMinus() + game.getScoreTeamOne());
                if (game.getWinnerTeam() == 2)
                    rankingItem.setWins(rankingItem.getWins() + 3);
                if (game.getWinnerTeam() == 0)
                    rankingItem.setWins(rankingItem.getWins() + 1);
            }
            if (rankingItem.getGames() > 0) {
                rankingItem.setWinsPerGame(((float) rankingItem.getWins()/ (float) rankingItem.getGames()));
                rankingItem.setScorePerGame(((float) rankingItem.getScorePlus() / (float) rankingItem.getGames()));
            } else {
                rankingItem.setWinsPerGame(0F);
                rankingItem.setScorePerGame(0F);
            }
            rankingItem.setActive(this.playerService.getActivity(rankingItem.getId()));
            ranking.add(rankingItem);
        }
        Collections.sort(ranking);
        return ranking;
    }

    public String getGamesCount() {
        return this.gameService.countAllGames().toString();

    }
}
