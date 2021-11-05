package webapp.kickerdb.kickerGame;

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
    private int scorePlus;
    private int scoreMinus;

    public KickerRankingItem(Long id, String userName, int wins, int games, int scorePlus, int scoreMinus) {
        this.id = id;
        this.userName = userName;
        this.wins = wins;
        this.games = games;
        this.scorePlus = scorePlus;
        this.scoreMinus = scoreMinus;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof KickerRankingItem) {
            KickerRankingItem other = (KickerRankingItem) o;
            if (this.wins < other.wins) {
                return 1;
            }
            if (this.wins > other.wins) {
                return -1;
            }
            if (this.wins == other.wins) {
                if (this.scorePlus < other.scorePlus) {
                    return 1;
                }
                if (this.scorePlus > other.scorePlus) {
                    return -1;
                }
            }
        }
        return 0;
    }
}
