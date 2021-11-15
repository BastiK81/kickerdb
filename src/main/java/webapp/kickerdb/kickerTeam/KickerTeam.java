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

    public KickerTeam() {
    }

    public KickerTeam(Long id,
                      Long playerDefensive,
                      Long playerOffensive) {
        super();
        this.id = id;
        this.playerDefensive = playerDefensive;
        this.playerOffensive = playerOffensive;
    }

    public KickerTeam(Long playerDefensive,
                      Long playerOffensive) {
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
