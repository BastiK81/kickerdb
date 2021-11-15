package webapp.kickerdb.kickerTeam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerPlayer.KickerPlayer;

import java.util.List;
import java.util.Optional;

@Service
public class KickerTeamService {

    @Autowired
    private KickerTeamRepository teamRepository;

    public Long getKickerTeamId(Long playerDefensive, Long playerOffensive) {
        List<KickerTeam> kickerTeam = teamRepository.findByPlayerDefensiveOrPlayerOffensive(playerDefensive, playerOffensive);
        if (!kickerTeam.isEmpty())
            return kickerTeam.get(0).getId();
        return addKickerTeam(new KickerTeam(playerDefensive, playerOffensive));
    }

    private Long addKickerTeam(KickerTeam kickerTeam){
        KickerTeam savedTeam = teamRepository.save(kickerTeam);
        return savedTeam.getId();
    }

    public List<KickerTeam> findAllTeamsWithPlayer(Long id) {
        return teamRepository.findByPlayerDefensiveOrPlayerOffensive(id, id);
    }

    public List<KickerTeam> getAllTeamsWithPlayerId(Long idOne, Long idTwo) {
        List<KickerTeam> allTeams = teamRepository.findByPlayerDefensiveAndPlayerOffensiveOrPlayerDefensiveAndPlayerOffensive(idOne, idTwo, idTwo, idOne);
        return allTeams;
    }

    public void addAllPossibleTeamsOfPlayerList(List<KickerPlayer> allPlayers) {
        for (int i = 0; i < allPlayers.size(); i++) {
            for (int j = 0; j < allPlayers.size(); j++) {
                if (i == j)
                    continue;
                KickerPlayer defensivePlayer = allPlayers.get(i);
                KickerPlayer offensivePlayer = allPlayers.get(j);
                if (teamRepository.findByPlayerDefensiveAndPlayerOffensive(defensivePlayer.getId(), offensivePlayer.getId()).isEmpty())
                    teamRepository.save(new KickerTeam(defensivePlayer.getId(),offensivePlayer.getId()));
            }
        }
    }

    public List<KickerTeam> getAllPossibleTeams() {
        return teamRepository.findAll();
    }
}
