package webapp.kickerdb.kickerRanking;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KickerRankingItem implements Comparable{

    private Long id;
    private String userName;
    private int wins;
    private int games;
    private Float winsPerGame;
    private int scorePlus;
    private Float scorePerGame;
    private int scoreMinus;
    private boolean active;

    public KickerRankingItem(Long id, String userName, int wins, int games, Float winsPerGame, int scorePlus, Float scorePerGame, int scoreMinus, boolean active) {
        this.id = id;
        this.userName = userName;
        this.wins = wins;
        this.games = games;
        this.winsPerGame = winsPerGame;
        this.scorePlus = scorePlus;
        this.scorePerGame = scorePerGame;
        this.scoreMinus = scoreMinus;
        this.active = active;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof KickerRankingItem) {
            KickerRankingItem other = (KickerRankingItem) o;
            if (this.winsPerGame < other.winsPerGame) {
                return 1;
            }
            if (this.winsPerGame > other.winsPerGame) {
                return -1;
            }
            if (this.wins < other.scorePerGame) {
                return 1;
            }
            if (this.wins > other.scorePerGame) {
                return -1;
            }
        }
        return 0;
    }
}
