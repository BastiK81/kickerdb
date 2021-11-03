package webapp.kickerdb.kickerGame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import webapp.kickerdb.kickerPlayer.KickerPlayer;

@Controller
public class KickerGameController {

    @Autowired
    private KickerGameService kickerGameService;

    @PostMapping(path = "/kickerGame/addGame", consumes = "application/json")
    public ResponseEntity<String> addUser(@RequestBody KickerGameRequest kickerGameRequest) {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain");
        String response = kickerGameService.addKickerGame(kickerGameRequest);
        return new ResponseEntity<>(response, responseHeaders, responseCode);
    }
}
