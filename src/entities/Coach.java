package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllCoaches",
                query = "SELECT c FROM Coach c ORDER BY c.name" // JPQL
        )
})
public class Coach extends User implements Serializable {
    @ManyToMany(mappedBy = "coaches")
    private Set<Sport> sports;

    public Coach() {
        this.sports = new LinkedHashSet<>();
    }

    public Coach(String username, String password, String name, String email) {
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
