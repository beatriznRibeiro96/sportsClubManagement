package entities;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;
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
    @ManyToMany(mappedBy = "athletes")
    private Set<Sport> sports;
    public Athlete() {
        this.sports = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.sports = new LinkedHashSet<>();
    }

    public Set<Sport> getSports() {
        return sports;
    }

    public void setSports(Set<Sport> sports) {
        this.sports = sports;
    }

    public void addSport(Sport sport) {
        sports.add(sport);
    }

    public void removeSport(Sport sport){
        sports.remove(sport);
    }
}
