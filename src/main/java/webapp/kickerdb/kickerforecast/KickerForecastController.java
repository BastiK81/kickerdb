package webapp.kickerdb.kickerforecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import webapp.kickerdb.kickerPlayer.KickerPlayer;

import java.util.List;

@Controller
public class KickerForecastController {

    @Autowired
    public KickerForecastService forecastService;

    @GetMapping(path = "/kickerForecast/getTeamForecast")
    public ResponseEntity<List<KickerPlayer>> getTeamForecast() {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        List<KickerPlayer> responseItem = forecastService.getTeamForecast();
        return new ResponseEntity<>(responseItem, responseHeaders, responseCode);
    }

    @GetMapping(path = "/kickerForecast/getPlayerForecast")
    public ResponseEntity<List<KickerPlayer>> getPlayerForecast() {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        List<KickerPlayer> responseItem = forecastService.getPlayerForecast();
        return new ResponseEntity<>(responseItem, responseHeaders, responseCode);
    }

    @GetMapping(path = "/kickerForecast/getGameForecast")
    public ResponseEntity<List<KickerPlayer>> getGameForecast() {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        List<KickerPlayer> responseItem = forecastService.getGameForecast();
        return new ResponseEntity<>(responseItem, responseHeaders, responseCode);
    }
}
