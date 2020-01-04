package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="TRAININGS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllTrainings",
                query = "SELECT t FROM Training t ORDER BY t.name"
        ),
        @NamedQuery(
                name = "countTrainingByName",
                query = "SELECT count(t) FROM Training t WHERE t.name = :name"
        )
})
public class Training implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Rank rank;

    @ManyToOne
    private Schedule schedule;

    @ManyToMany
    private Set<Athlete> presences;

    @Version
    private int version;

    public Training() {
        this.presences = new LinkedHashSet<>();
    }

    public Training(String name, Rank rank, Schedule schedule) {
        this.name = name;
        this.rank = rank;
        this.schedule = schedule;
        this.presences = new LinkedHashSet<>();
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Set<Athlete> getPresences() {
        return presences;
    }

    public void setPresences(Set<Athlete> presences) {
        this.presences = presences;
    }

    public void addPresence(Athlete presence){
        presences.add(presence);
    }

    public void removePresence(Athlete presence){
        presences.remove(presence);
    }
}
