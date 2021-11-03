package webapp.kickerdb.kickerPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class KickerPlayerController {

    @Autowired
    private KickerPlayerService kickerPlayerService;

    @PostMapping(path = "/kickerplayer/addPlayer", consumes = "application/json")
    public ResponseEntity<String> addUser(@RequestBody KickerPlayer kickerPlayer) {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain");
        String response = kickerPlayerService.addKickerPlayer(kickerPlayer);
        return new ResponseEntity<>(response, responseHeaders, responseCode);
    }
}
