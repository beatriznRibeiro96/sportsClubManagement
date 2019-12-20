package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="SPORT_SUBSCRIPTIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"ACTIVESPORT_CODE", "ATHLETE_USERNAME"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllSportSubscriptions",
                query = "SELECT ss FROM SportSubscription ss ORDER BY ss.code"
        ),
        @NamedQuery(
                name = "countSportSubscriptionByActiveSportAndAthlete",
                query = "SELECT count(ss) FROM SportSubscription ss WHERE ss.activeSport = :activeSport AND ss.athlete = :athlete"
        )
})
public class SportSubscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    private String name;
    @ManyToOne
    private ActiveSport activeSport;
    @ManyToOne
    private Athlete athlete;
    @Version
    private int version;

    public SportSubscription() {
    }

    public SportSubscription(String name, ActiveSport activeSport, Athlete athlete) {
        this.name = name;
        this.activeSport = activeSport;
        this.athlete = athlete;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
