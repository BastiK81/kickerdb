package webapp.kickerdb.kickerPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KickerPlayerService {

    @Autowired
    private KickerPlayerRepository kickerPlayerRepository;

    public String addKickerPlayer(KickerPlayer kickerPlayer) {
        if (kickerPlayerRepository.findByUserName(kickerPlayer.getUserName()).isPresent())
            return "Player already exist";
        kickerPlayerRepository.save(kickerPlayer);
        if (kickerPlayerRepository.findByUserName(kickerPlayer.getUserName()).isPresent())
            return "Player saved";
        return "Player could not bne saved";
    }

    public Long getKickerPlayerId(String userName){
        Optional<KickerPlayer> kickerPlayer = kickerPlayerRepository.findByUserName(userName);
        if (kickerPlayer.isPresent())
            return kickerPlayer.get().getId();
        return 0L;
    }
}
