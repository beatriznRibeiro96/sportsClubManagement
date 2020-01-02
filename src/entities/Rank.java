package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="RANKS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "ACTIVESPORT_CODE"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllRanks",
                query = "SELECT r FROM Rank r ORDER BY r.name"
        ),
        @NamedQuery(
                name = "countRanksByNameAndActiveSport",
                query = "SELECT count(r) FROM Rank r WHERE r.name = :name AND r.activeSport = :activeSport"
        )
})
public class Rank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "minimum age is mandatory")
    @Column(nullable = false)
    private int idadeMin;
    @NotNull(message = "maximum age is mandatory")
    @Column(nullable = false)
    private int idadeMax;
    @NotNull(message = "active sport is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private ActiveSport activeSport;
    @OneToMany(mappedBy = "rank", cascade = CascadeType.REMOVE)
    private Set<SportSubscription> sportSubscriptions;
    @ManyToMany
    @JoinTable(name = "RANKS_COACHES",
            joinColumns = @JoinColumn(name = "RANK_CODE", referencedColumnName = "CODE", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "COACH_USERNAME", referencedColumnName =
                    "USERNAME", nullable = false))
    private Set<Coach> coaches;

    @OneToMany(mappedBy = "rank", cascade = CascadeType.REMOVE)
    private Set<Schedule> schedules;

    @Version
    private int version;

    public Rank() {
        this.coaches = new LinkedHashSet<>();
        this.sportSubscriptions = new LinkedHashSet<>();
        this.schedules = new LinkedHashSet<>();
    }

    public Rank(String name, int idadeMin, int idadeMax, ActiveSport activeSport) {
        this.sportSubscriptions = new LinkedHashSet<>();
        this.coaches = new LinkedHashSet<>();
        this.name = name;
        this.idadeMin = idadeMin;
        this.idadeMax = idadeMax;
        this.activeSport = activeSport;
        this.schedules = new LinkedHashSet<>();
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

    public int getIdadeMin() {
        return idadeMin;
    }

    public void setIdadeMin(int idadeMin) {
        this.idadeMin = idadeMin;
    }

    public int getIdadeMax() {
        return idadeMax;
    }

    public void setIdadeMax(int idadeMax) {
        this.idadeMax = idadeMax;
    }

    public ActiveSport getActiveSport() {
        return activeSport;
    }

    public void setActiveSport(ActiveSport activeSport) {
        this.activeSport = activeSport;
    }

    public Set<SportSubscription> getSportSubscriptions() {
        return sportSubscriptions;
    }

    public void setSportSubscriptions(Set<SportSubscription> sportSubscriptions) {
        this.sportSubscriptions = sportSubscriptions;
    }

    public Set<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(Set<Coach> coaches) {
        this.coaches = coaches;
    }

    public Set<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void addSportSubscription(SportSubscription sportSubscription) {
        sportSubscriptions.add(sportSubscription);
    }

    public void removeSportSubscription(SportSubscription sportSubscription){
        sportSubscriptions.remove(sportSubscription);
    }

    public void addCoach(Coach coach) {
        coaches.add(coach);
    }

    public void removeCoach(Coach coach){
        coaches.remove(coach);
    }

    public void addSchedule(Schedule schedule) { schedules.add(schedule); }

    public void removeSchedule(Schedule schedule) { schedules.remove(schedule); }
}
