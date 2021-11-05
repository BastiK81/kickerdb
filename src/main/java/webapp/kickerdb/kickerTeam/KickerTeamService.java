package webapp.kickerdb.kickerTeam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KickerTeamService {

    @Autowired
    private KickerTeamRepository kickerTeamRepository;

    public Long getKickerTeamId(Long playerDefensive, Long playerOffensive, int side) {
        Optional<KickerTeam> kickerTeam = kickerTeamRepository.findByPlayerDefensiveAndPlayerOffensiveAndSide(playerDefensive, playerOffensive, side);
        if (kickerTeam.isPresent())
            return kickerTeam.get().getId();
        return addKickerTeam(new KickerTeam(playerDefensive, playerOffensive, side));
    }

    private Long addKickerTeam(KickerTeam kickerTeam){
        KickerTeam savedTeam = kickerTeamRepository.save(kickerTeam);
        return savedTeam.getId();
    }

    public List<KickerTeam> findAllTeamsWithPlayer(Long id) {
        return kickerTeamRepository.findByPlayerDefensiveOrPlayerOffensive(id, id);
    }
}
