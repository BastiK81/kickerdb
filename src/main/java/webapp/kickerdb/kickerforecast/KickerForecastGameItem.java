package webapp.kickerdb.kickerforecast;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@EqualsAndHashCode
@Entity
@Table(name = "kickerForecastGames")
@Getter
public class KickerForecastGameItem implements Comparable{

    @SequenceGenerator(
            name = "kickerForecastGameSequence",
            sequenceName = "kickerForecastGameSequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kickerForecastGameSequence"
    )
    private Long id;
    private Long teamOneId;
    private Long teamOneDefensiveId;
    private Long teamOneOffensiveId;
    private Long teamTwoId;
    private Long teamTwoDefensiveId;
    private Long teamTwoOffensiveId;
    private int games;
    private int teamGames;
    private int playerGames;

    public KickerForecastGameItem() {
    }

    public KickerForecastGameItem(Long teamOneId, Long teamOneDefensiveId, Long teamOneOffensiveId, Long teamTwoId, Long teamTwoDefensiveId, Long teamTwoOffensiveId, int games, int teamGames, int playerGames) {
        super();
        this.teamOneId = teamOneId;
        this.teamOneDefensiveId = teamOneDefensiveId;
        this.teamOneOffensiveId = teamOneOffensiveId;
        this.teamTwoId = teamTwoId;
        this.teamTwoDefensiveId = teamTwoDefensiveId;
        this.teamTwoOffensiveId = teamTwoOffensiveId;
        this.games = games;
        this.teamGames = teamGames;
        this.playerGames = playerGames;
    }

    public KickerForecastGameItem(Long teamOneId, Long teamOneDefensiveId, Long teamOneOffensiveId, Long teamTwoId, Long teamTwoDefensiveId, Long teamTwoOffensiveId) {
        super();
        this.teamOneId = teamOneId;
        this.teamOneDefensiveId = teamOneDefensiveId;
        this.teamOneOffensiveId = teamOneOffensiveId;
        this.teamTwoId = teamTwoId;
        this.teamTwoDefensiveId = teamTwoDefensiveId;
        this.teamTwoOffensiveId = teamTwoOffensiveId;
        this.games = 0;
        this.teamGames = 0;
        this.playerGames = 0;
    }

    public Long getId() {
        return id;
    }

    public Long getTeamOneDefensiveId() {
        return teamOneDefensiveId;
    }

    public Long getTeamOneOffensiveId() {
        return teamOneOffensiveId;
    }

    public Long getTeamTwoDefensiveId() {
        return teamTwoDefensiveId;
    }

    public Long getTeamTwoOffensiveId() {
        return teamTwoOffensiveId;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof KickerForecastGameItem) {
            KickerForecastGameItem other = (KickerForecastGameItem) o;
            if (this.games < other.games) {
                return -1;
            }
            if (this.games > other.games) {
                return 1;
            }
            if (this.teamGames < other.teamGames) {
                return -1;
            }
            if (this.teamGames > other.teamGames) {
                return 1;
            }
            if (this.playerGames < other.playerGames) {
                return -1;
            }
            if (this.playerGames > other.playerGames) {
                return 1;
            }
        }
        return 0;
    }
}
