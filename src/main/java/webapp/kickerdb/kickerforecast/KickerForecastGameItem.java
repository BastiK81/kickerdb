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
    private int vsGames;
    private int teamOneGames;
    private int teamTwoGames;
    private int teamGames;
    private int teamConstGames;
    private int playerGames;

    public KickerForecastGameItem() {
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
        this.teamOneGames = 0;
        this.teamTwoGames = 0;
        this.vsGames = 0;
        this.teamConstGames = 0;
    }

    public int getTeamOneGames() {
        return teamOneGames;
    }

    public int getTeamTwoGames() {
        return teamTwoGames;
    }

    public void setTeamConstGames(int teamConstGames) {
        this.teamConstGames = teamConstGames;
    }

    public void setTeamOneGames(int teamOneGames) {
        this.teamOneGames = teamOneGames;
    }

    public void setTeamTwoGames(int teamTwoGames) {
        this.teamTwoGames = teamTwoGames;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public void setTeamGames(int teamGames) {
        this.teamGames = teamGames;
    }

    public void setPlayerGames(int playerGames) {
        this.playerGames = playerGames;
    }

    public void setVsGames(int vsGames) {
        this.vsGames = vsGames;
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
            if (this.vsGames < other.vsGames) {
                return -1;
            }
            if (this.vsGames > other.vsGames) {
                return 1;
            }
            if (this.teamGames < other.teamGames) {
                return -1;
            }
            if (this.teamGames > other.teamGames) {
                return 1;
            }
            if (this.teamConstGames < other.teamConstGames) {
                return -1;
            }
            if (this.teamConstGames > other.teamConstGames) {
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
