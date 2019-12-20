package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="ACTIVE_SPORTS", uniqueConstraints = @UniqueConstraint(columnNames = {"SEASON_CODE", "SPORT_CODE"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveSports",
                query = "SELECT acs FROM ActiveSport acs ORDER BY acs.name"
        ),
        @NamedQuery(
                name = "countActiveSportBySportAndSeason",
                query = "SELECT count(acs) FROM ActiveSport acs WHERE acs.sport = :sport AND acs.season = :season"
        )
})
public class ActiveSport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    private String name;

    @ManyToOne
    private Sport sport;

    @ManyToOne
    private Season season;

    @ManyToMany
    @JoinTable(name = "ACTIVE_SPORTS_COACHES",
            joinColumns = @JoinColumn(name = "ACTIVE_SPORT_CODE", referencedColumnName = "CODE", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "COACH_USERNAME", referencedColumnName =
                    "USERNAME", nullable = false))
    private Set<Coach> coaches;
    @Version
    private int version;

    public ActiveSport() {
        this.coaches = new LinkedHashSet<>();
    }

    public ActiveSport(String name, Sport sport, Season season) {
        this.name = name;
        this.sport = sport;
        this.season = season;
        this.coaches = new LinkedHashSet<>();
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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Set<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(Set<Coach> coaches) {
        this.coaches = coaches;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void addCoach(Coach coach) {
        coaches.add(coach);
    }

    public void removeCoach(Coach coach){
        coaches.remove(coach);
    }
}
