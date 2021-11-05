package webapp.kickerdb.kickerforecast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KickerForecastTeam implements Comparable {

    private Long idOne;
    private Long idTwo;
    private Long playerDefensiveId;
    private Long playerOffensiveId;
    private int games;
    private int singleGames;

    public KickerForecastTeam(Long playerDefensiveId, Long playerOffensiveId) {
        this.playerDefensiveId = playerDefensiveId;
        this.playerOffensiveId = playerOffensiveId;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof KickerForecastTeam) {
            KickerForecastTeam other = (KickerForecastTeam) o;
            if (this.games < other.games) {
                return -1;
            }
            if (this.games > other.games) {
                return 1;
            }
            if (this.games == other.games) {
                if (this.singleGames < other.singleGames) {
                    return -1;
                }
                if (this.singleGames > other.singleGames) {
                    return 1;
                }
            }
        }
        return 0;
    }

}
