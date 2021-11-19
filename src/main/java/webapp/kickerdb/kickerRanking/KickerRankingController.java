package webapp.kickerdb.kickerRanking;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class KickerRankingController {

    private KickerRankingService rankingService;

    @GetMapping(path = "/kickerGame/ranking")
    public ResponseEntity<List<KickerRankingItem>> getRanking() {
        HttpStatus responseCode = HttpStatus.OK;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        List<KickerRankingItem> responseItem = this.rankingService.getRanking();
        return new ResponseEntity<>(responseItem, responseHeaders, responseCode);
    }
}
