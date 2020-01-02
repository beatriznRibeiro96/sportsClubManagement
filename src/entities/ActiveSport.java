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

    @OneToMany(mappedBy = "activeSport", cascade = CascadeType.REMOVE)
    private Set<Rank> ranks;

    @OneToMany(mappedBy = "activeSport", cascade = CascadeType.REMOVE)
    private Set<Grade> grades;

    @Version
    private int version;

    public ActiveSport() {
        this.ranks = new LinkedHashSet<>();
        this.grades = new LinkedHashSet<>();
    }

    public ActiveSport(String name, Sport sport, Season season) {
        this.name = name;
        this.sport = sport;
        this.season = season;
        this.ranks = new LinkedHashSet<>();
        this.grades = new LinkedHashSet<>();
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

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public void addRank(Rank rank) { ranks.add(rank); }

    public void removeRank(Rank rank) { ranks.remove(rank); }

    public void addGrade(Grade grade) { grades.add(grade); }

    public void removeGrade(Grade grade) { grades.remove(grade); }
}
