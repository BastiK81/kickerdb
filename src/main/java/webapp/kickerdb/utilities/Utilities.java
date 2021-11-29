package webapp.kickerdb.utilities;

import webapp.kickerdb.kickerGame.KickerGameRequest;
import webapp.kickerdb.kickerTeam.KickerTeam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utilities {

    public boolean hasTeamsDoublePlayer(KickerTeam teamOne, KickerTeam teamTwo) {
        List<Long> players = new ArrayList<>();
        players.add(teamOne.getPlayerDefensive());
        players.add(teamOne.getPlayerOffensive());
        if (players.contains(teamTwo.getPlayerDefensive()))
            return true;
        players.add(teamTwo.getPlayerDefensive());
        if (players.contains(teamTwo.getPlayerOffensive()))
            return true;
        return false;
    }

    public boolean hasGameRequestDoublePlayer(KickerGameRequest kickerGameRequest){
        List<String> players = new ArrayList<>();
        players.add(kickerGameRequest.getPlayerOne());
        if (players.contains(kickerGameRequest.getPlayerTwo()))
            return true;
        players.add(kickerGameRequest.getPlayerTwo());
        if (players.contains(kickerGameRequest.getPlayerThree()))
            return true;
        players.add(kickerGameRequest.getPlayerThree());
        if (players.contains(kickerGameRequest.getPlayerFour()))
            return true;
        players.add(kickerGameRequest.getPlayerFour());
        return false;
    }

    public int getWinner(int scoreOne, int scoreTwo) {
        if (scoreOne > scoreTwo)
            return  1;
        if (scoreTwo > scoreOne)
            return 2;
        return 0;
    }

    public  <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();
        set.addAll(list1);
        set.addAll(list2);
        return new ArrayList<T>(set);
    }
}
