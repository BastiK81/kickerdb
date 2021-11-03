package webapp.kickerdb.kickerPlayer;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@Entity
@Table (name = "kickerPlayer")
@Getter
public class KickerPlayer {

    @SequenceGenerator(
            name = "kickerPlayerSequence",
            sequenceName = "kickerPlayerSequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kickerPlayerSequence"
    )
    private Long id;
    private String userName;

    public KickerPlayer(String userName) {
        this.userName = userName;
    }

    public KickerPlayer() {
    }

    public KickerPlayer(Long id, String userName) {
        super();
        this.id = id;
        this.userName = userName;
    }
}
