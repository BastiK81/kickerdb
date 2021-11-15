package webapp.kickerdb.kickerPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerTeam.KickerTeam;
import webapp.kickerdb.kickerTeam.KickerTeamService;
import webapp.kickerdb.kickerforecast.KickerForecastService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class KickerPlayerService {

    @Autowired
    private KickerPlayerRepository playerRepository;

    @Autowired
    private KickerTeamService teamService;

    @Autowired
    private KickerForecastService forecastService;

    private static final String PLAYER_NOT_FOUND = "Player %s not found";

    public String addKickerPlayer(KickerPlayer kickerPlayer) {
        if (this.playerRepository.findByUserName(kickerPlayer.getUserName()).isPresent())
            return String.format("Player %s already exist", kickerPlayer.getUserName());
        kickerPlayer.setActive(true);
        this.playerRepository.save(kickerPlayer);
        if (this.playerRepository.findByUserName(kickerPlayer.getUserName()).isPresent()) {
            List<KickerPlayer> allPlayers = this.getAll();
            this.teamService.addAllPossibleTeamsOfPlayerList(allPlayers);
            List<KickerTeam> allPossibleTeams = this.teamService.getAllPossibleTeams();
            this.forecastService.addAllPossibleGames(allPossibleTeams);
            return String.format("Player %s added", kickerPlayer.getUserName());
        }
        return String.format("Player %s could not be saved", kickerPlayer.getUserName());
    }

    public Long getKickerPlayerId(String userName){
        Optional<KickerPlayer> kickerPlayer = this.playerRepository.findByUserName(userName);
        if (kickerPlayer.isPresent())
            return kickerPlayer.get().getId();
        return 0L;
    }

    public List<KickerPlayer> getAll() {
        return playerRepository.findAll();
    }

    public List<KickerPlayer> getAllActive() {
        return playerRepository.findByActive(true);
    }

    public String delete(KickerPlayer kickerPlayer) {
        Long id = getKickerPlayerId(kickerPlayer.getUserName());
        if (id == 0L)
            return String.format(PLAYER_NOT_FOUND, kickerPlayer.getUserName());;
        playerRepository.deleteById(id);
        return String.format("Player %s deleted", kickerPlayer.getUserName());
    }

    @Transactional
    public String changeActivity(KickerPlayer kickerPlayer) {
        Long id = getKickerPlayerId(kickerPlayer.getUserName());
        if (id == 0L)
            return String.format(PLAYER_NOT_FOUND, kickerPlayer.getUserName());
        Optional<KickerPlayer> player = playerRepository.findById(id);
        player.get().setActive(!player.get().isActive());
        return String.format("Activity for Player %s changed", kickerPlayer.getUserName());
    }

    public String getPlayerNameByID(Long id) {
        return playerRepository.getById(id).getUserName();
    }

    public KickerPlayer getPlayerByID(Long id) {
        return playerRepository.getById(id);
    }
}
