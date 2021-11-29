package webapp.kickerdb.kickerPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KickerPlayerCommunicator {

    @Autowired
    private KickerPlayerRepository repository;

    public boolean hasPlayerWithName(String name) {
        return repository.findByUserName(name).isPresent();
    }

    public boolean hasPlayerWithId(Long id) {
        return repository.existsById(id);
    }

    public Long getPlayerIdByName(String name) {
        return repository.findByUserName(name).get().getId();
    }

    public KickerPlayer getPlayerByName(String name) {
        return repository.findByUserName(name).get();
    }

    public List<KickerPlayer> getAllPlayer() {
        return repository.findAll();
    }

    public List<KickerPlayer> getAllActivePlayer(){
        return repository.findByActive(true);
    }

    public Long savePlayer(KickerPlayer player) {
        return repository.save(player).getId();
    }

    public void deletePlayerById(Long id) {
        repository.deleteById(id);
    }

    public KickerPlayer getPlayerById(Long id) {
        return this.repository.findById(id).get();
    }

    public boolean getPlayerActivity(Long id) {
        return this.repository.findById(id).get().isActive();
    }
}
