package webapp.kickerdb.kickerGame;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class KickerGameController {

    @Autowired
    private KickerGameService gameService;

    @PostMapping(path = "/kickerGame/addGame", consumes = "text/plain")
    public ResponseEntity<String> addGame(@RequestBody String gameSet) {
        Gson gson = new Gson();
        KickerGameRequest kickerGameRequest = gson.fromJson(gameSet, KickerGameRequest.class);
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain");
        String response = gameService.addKickerGame(kickerGameRequest);
        return new ResponseEntity<>(response, responseHeaders, responseCode);
    }
}
