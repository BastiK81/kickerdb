package webapp.kickerdb.navigation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NavigationController {

    @GetMapping(path = "/index")
    public String getIndexView(){
        return "index";
    }

    @GetMapping(path = "/addplayer")
    public String getAddPlayerView(){
        return "addplayer";
    }

    @GetMapping(path = "/addgame")
    public String getAddGameView(){
        return "addgame";
    }
}
