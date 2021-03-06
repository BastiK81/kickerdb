package webapp.kickerdb.kickerPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
public class KickerPlayerController {

    @Autowired
    private KickerPlayerService playerService;

    @PostMapping(path = "/kickerPlayer/addPlayer", consumes = "application/json")
    public ResponseEntity<String> addUser(@RequestBody KickerPlayer kickerPlayer) {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain");
        String response = playerService.addKickerPlayer(kickerPlayer);
        return new ResponseEntity<>(response, responseHeaders, responseCode);
    }

    @GetMapping(path = "/kickerPlayer/getAll")
    public ResponseEntity<List<KickerPlayer>> getAll(){
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        List<KickerPlayer> responseList = playerService.getAllPlayer();
        return new ResponseEntity<>(responseList, responseHeaders, responseCode);
    }

    @GetMapping(path = "/kickerPlayer/getAllActive")
    public ResponseEntity<List<KickerPlayer>> getAllActive(){
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        List<KickerPlayer> responseList = playerService.getAllActivePlayer();
        return new ResponseEntity<>(responseList, responseHeaders, responseCode);
    }

    @PostMapping(path = "/kickerPlayer/delete", consumes = "application/json")
    public ResponseEntity<String> deleteUser(@RequestBody KickerPlayer kickerPlayer) {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain");
        String response = playerService.deletePlayerByName(kickerPlayer.getUserName());
        return new ResponseEntity<>(response, responseHeaders, responseCode);
    }

    @PostMapping(path = "/kickerPlayer/changeActivity", consumes = "application/json")
    public ResponseEntity<String> changeActivityUser(@RequestBody KickerPlayer kickerPlayer) {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain");
        String response = playerService.changePlayerActivityByName(kickerPlayer.getUserName());
        return new ResponseEntity<>(response, responseHeaders, responseCode);
    }
}
