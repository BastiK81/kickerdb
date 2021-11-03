package webapp.kickerdb.kickerGame;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KickerGameRequest {

    private String playerOne;
    private String playerTwo;
    private String playerThree;
    private String playerFour;
    private int scoreOne;
    private int scoreTwo;

    public KickerGameRequest(String playerOne, String playerTwo, String playerThree, String playerFour, int scoreOne, int scoreTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerThree = playerThree;
        this.playerFour = playerFour;
        this.scoreOne = scoreOne;
        this.scoreTwo = scoreTwo;
    }
}
