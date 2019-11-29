package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="SPORTS")
@NamedQueries({
        @NamedQuery(
                name = "getAllSports",
                query = "SELECT s FROM Sport s ORDER BY s.name"
        )
})
public class Sport implements Serializable {
    @Id
    private int code;
    private String name;
    @ManyToMany
    @JoinTable(name = "SPORTS_COACHES",
            joinColumns = @JoinColumn(name = "SPORT_CODE", referencedColumnName = "CODE", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "COACH_USERNAME", referencedColumnName =
                    "USERNAME", nullable = false))
    private Set<Coach> coaches;
    @Version
    private int version;

    public Sport() {
        this.coaches = new LinkedHashSet<>();
    }

    public Sport(int code, String name) {
        this.code = code;
        this.name = name;
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

    public Set<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(Set<Coach> coaches) {
        this.coaches = coaches;
    }

    public void addCoach(Coach coach) {
        coaches.add(coach);
    }

    public void removeCoach(Coach coach){
        coaches.remove(coach);
    }
}
