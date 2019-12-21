package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "active sport is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private ActiveSport activeSport;
    @NotNull(message = "athlete is mandatory")
    @JoinColumn(nullable = false)
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
