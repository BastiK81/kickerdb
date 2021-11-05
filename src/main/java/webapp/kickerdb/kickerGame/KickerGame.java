package webapp.kickerdb.kickerGame;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "kickerGames")
@Getter
public class KickerGame {

    @SequenceGenerator(
            name = "kickerGameSequence",
            sequenceName = "kickerGameSequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kickerGameSequence"
    )
    private Long id;
    private Long teamOne;
    private int scoreTeamOne;
    private Long teamTwo;
    private int scoreTeamTwo;
    private int winnerTeam;
    private LocalDate gameDay;

    public KickerGame() {
    }

    public KickerGame(Long id, Long teamOne, int scoreTeamOne, Long teamTwo, int scoreTeamTwo, int winnerTeam, LocalDate gameDay) {
        this.id = id;
        this.teamOne = teamOne;
        this.scoreTeamOne = scoreTeamOne;
        this.teamTwo = teamTwo;
        this.scoreTeamTwo = scoreTeamTwo;
        this.winnerTeam = winnerTeam;
        this.gameDay = gameDay;
    }

    public KickerGame(Long teamOne, int scoreTeamOne, Long teamTwo, int scoreTeamTwo, int winnerTeam) {
        this.teamOne = teamOne;
        this.scoreTeamOne = scoreTeamOne;
        this.teamTwo = teamTwo;
        this.scoreTeamTwo = scoreTeamTwo;
        this.winnerTeam = winnerTeam;
        this.gameDay = LocalDate.now();
    }
}
