package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAthletes",
                query = "SELECT at FROM Athlete at ORDER BY at.name" // JPQL
        )
})
public class Athlete extends Partner implements Serializable {
    @OneToMany(mappedBy = "athlete", cascade = CascadeType.REMOVE)
    private Set<SportSubscription> sportSubscriptions;
    @ManyToMany(mappedBy = "athletes")
    private Set<Grade> grades;

    public Athlete() {
        this.sportSubscriptions = new LinkedHashSet<>();
        this.grades = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email, LocalDate birthDate) {
        super(username, password, name, email, birthDate);
        this.sportSubscriptions = new LinkedHashSet<>();
        this.grades = new LinkedHashSet<>();
    }

    public Set<SportSubscription> getSportSubscriptions() {
        return sportSubscriptions;
    }

    public void setSportSubscriptions(Set<SportSubscription> sportSubscriptions) {
        this.sportSubscriptions = sportSubscriptions;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    public void addSportSubscription(SportSubscription sportSubscription) {
        sportSubscriptions.add(sportSubscription);
    }

    public void removeSportSubscription(SportSubscription sportSubscription){
        sportSubscriptions.remove(sportSubscription);
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public void removeGrade(Grade grade){
        grades.remove(grade);
    }
}
