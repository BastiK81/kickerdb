package webapp.kickerdb.kickerGame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.kickerdb.kickerPlayer.KickerPlayerService;
import webapp.kickerdb.kickerTeam.KickerTeamRepository;
import webapp.kickerdb.kickerTeam.KickerTeamService;

@Service
public class KickerGameService {

    @Autowired
    private KickerPlayerService kickerPlayerService;

    @Autowired
    private KickerTeamService kickerTeamService;

    @Autowired
    private KickerGameRepository kickerGameRepository;

    public String addKickerGame(KickerGameRequest kickerGameRequest) {
        Long playerIdOne = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerOne());
        if (playerIdOne == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerOne());

        Long playerIdTwo = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerTwo());
        if (playerIdTwo == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerTwo());

        Long playerIdThree = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerThree());
        if (playerIdThree == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerThree());

        Long playerIdFour = kickerPlayerService.getKickerPlayerId(kickerGameRequest.getPlayerFour());
        if (playerIdFour == 0L)
            return String.format("Player %s not found", kickerGameRequest.getPlayerFour());

        Long teamOne = kickerTeamService.getKickerTeamId(playerIdOne, playerIdTwo, 1);
        Long teamTwo = kickerTeamService.getKickerTeamId(playerIdThree, playerIdFour, 2);

        int winner = 0;
        if (kickerGameRequest.getScoreOne() > kickerGameRequest.getScoreTwo())
            winner = 1;
        if (kickerGameRequest.getScoreTwo() > kickerGameRequest.getScoreOne())
            winner = 2;

        KickerGame kickerGame = new KickerGame(teamOne, kickerGameRequest.getScoreOne(), teamTwo, kickerGameRequest.getScoreTwo(), winner);
        kickerGameRepository.save(kickerGame);
        return "Game saved";
    }
}
