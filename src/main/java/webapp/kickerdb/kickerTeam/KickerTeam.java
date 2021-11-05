package webapp.kickerdb.kickerTeam;

import javax.persistence.*;

@Entity
@Table(name = "kickerTeams")
public class KickerTeam{

    @SequenceGenerator(
            name = "kickerTeamSequence",
            sequenceName = "kickerTeamSequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "kickerTeamSequence"
    )
    private Long id;

    private Long playerDefensive;
    private Long playerOffensive;
    private int side;

    public KickerTeam() {
    }

    public KickerTeam(Long id,
                      Long playerDefensive,
                      Long playerOffensive,
                      int side) {
        super();
        this.id = id;
        this.playerDefensive = playerDefensive;
        this.playerOffensive = playerOffensive;
        this.side = side;
    }

    public KickerTeam(Long playerDefensive,
                      Long playerOffensive,
                      int side) {
        super();
        this.playerDefensive = playerDefensive;
        this.playerOffensive = playerOffensive;
        this.side = side;
    }

    public KickerTeam(Long playerDefensive, Long playerOffensive) {
        super();
        this.playerDefensive = playerDefensive;
        this.playerOffensive = playerOffensive;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerDefensive() {
        return playerDefensive;
    }

    public Long getPlayerOffensive() {
        return playerOffensive;
    }
}
