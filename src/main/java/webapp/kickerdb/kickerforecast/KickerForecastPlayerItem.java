package webapp.kickerdb.kickerforecast;

public class KickerForecastPlayerItem implements Comparable{

    private Long id;
    private String userName;
    private int games;

    public KickerForecastPlayerItem(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof KickerForecastPlayerItem) {
            KickerForecastPlayerItem other = (KickerForecastPlayerItem) o;
            if (this.games < other.games) {
                return -1;
            }
            if (this.games > other.games) {
                return 1;
            }
        }
        return 0;
    }
}
