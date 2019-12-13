package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="SPORT_SUBSCRIPTIONS")
@NamedQueries({
        @NamedQuery(
                name = "getAllSportSubscriptions",
                query = "SELECT ss FROM SportSubscription ss ORDER BY ss.code"
        )
})
public class SportSubscription implements Serializable {
    @Id
    private int code;
    @ManyToOne
    private ActiveSport activeSport;
    @ManyToOne
    private Athlete athlete;

    public SportSubscription() {
    }

    public SportSubscription(int code, ActiveSport activeSport, Athlete athlete) {
        this.code = code;
        this.activeSport = activeSport;
        this.athlete = athlete;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ActiveSport getActiveSport() {
        return activeSport;
    }

    public void setActiveSport(ActiveSport activeSport) {
        this.activeSport = activeSport;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }
}
