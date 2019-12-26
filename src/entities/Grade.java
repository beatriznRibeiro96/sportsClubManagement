package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="GRADES", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "ACTIVESPORT_CODE"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllGrades",
                query = "SELECT g FROM Grade g ORDER BY g.name"
        ),
        @NamedQuery(
                name = "countGradesByNameAndActiveSport",
                query = "SELECT count(g) FROM Grade g WHERE g.name = :name AND g.activeSport = :activeSport"
        )
})
public class Grade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "active sport is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private ActiveSport activeSport;
    @ManyToMany
    private Set<Athlete> athletes;
    @JoinTable(name = "GRADES_ATHLETES",
            joinColumns = @JoinColumn(name = "GRADE_CODE", referencedColumnName = "CODE", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "ATHLETE_USERNAME", referencedColumnName =
                    "USERNAME", nullable = false))
    @Version
    private int version;

    public Grade() {
        this.athletes = new LinkedHashSet<>();
    }

    public Grade(String name, ActiveSport activeSport) {
        this.name = name;
        this.activeSport = activeSport;
        this.athletes = new LinkedHashSet<>();
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

    public Set<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(Set<Athlete> athletes) {
        this.athletes = athletes;
    }

    public void addAthlete(Athlete athlete) {
        athletes.add(athlete);
    }

    public void removeAthlete(Athlete athlete){
        athletes.remove(athlete);
    }
}
