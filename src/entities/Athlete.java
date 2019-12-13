package entities;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "athlete", cascade = CascadeType.REMOVE)
    private Set<SportSubscription> sportSubscriptions;
    public Athlete() {
        this.sportSubscriptions = new LinkedHashSet<>();
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.sportSubscriptions = new LinkedHashSet<>();
    }

    public Set<SportSubscription> getSportSubscriptions() {
        return sportSubscriptions;
    }

    public void setSportSubscriptions(Set<SportSubscription> sportSubscriptions) {
        this.sportSubscriptions = sportSubscriptions;
    }

    public void addSportSubscription(SportSubscription sportSubscription) {
        sportSubscriptions.add(sportSubscription);
    }

    public void removeSportSubscription(SportSubscription sportSubscription){
        sportSubscriptions.remove(sportSubscription);
    }
}
