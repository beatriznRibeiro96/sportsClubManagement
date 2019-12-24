package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "sport is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private Sport sport;
    @NotNull(message = "season is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private Season season;

    @ManyToMany
    @JoinTable(name = "ACTIVE_SPORTS_COACHES",
            joinColumns = @JoinColumn(name = "ACTIVE_SPORT_CODE", referencedColumnName = "CODE", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "COACH_USERNAME", referencedColumnName =
                    "USERNAME", nullable = false))
    private Set<Coach> coaches;

    @OneToMany(mappedBy = "activeSport", cascade = CascadeType.REMOVE)
    private Set<Rank> ranks;
    @Version
    private int version;

    public ActiveSport() {
        this.coaches = new LinkedHashSet<>();
        this.ranks = new LinkedHashSet<>();
    }

    public ActiveSport(String name, Sport sport, Season season) {
        this.name = name;
        this.sport = sport;
        this.season = season;
        this.coaches = new LinkedHashSet<>();
        this.ranks = new LinkedHashSet<>();
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

    public Set<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(Set<Rank> ranks) {
        this.ranks = ranks;
    }

    public void addCoach(Coach coach) {
        coaches.add(coach);
    }

    public void removeCoach(Coach coach){
        coaches.remove(coach);
    }

    public void addRank(Rank rank) { ranks.add(rank); }

    public void removeRank(Rank rank) { ranks.remove(rank); }
}
