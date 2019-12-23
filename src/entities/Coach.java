package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private Set<ActiveSport> activeSports;

    public Coach() {
        this.activeSports = new LinkedHashSet<>();
    }

    public Coach(String username, String password, String name, String email, LocalDate birthDate) {
        super(username, password, name, email, birthDate);
        this.activeSports = new LinkedHashSet<>();
    }

    public Set<ActiveSport> getActiveSports() {
        return activeSports;
    }

    public void setActiveSports(Set<ActiveSport> activeSports) {
        this.activeSports = activeSports;
    }

    public void addActiveSport(ActiveSport activeSport) {
        activeSports.add(activeSport);
    }

    public void removeActiveSport(ActiveSport activeSport){
        activeSports.remove(activeSport);
    }
}
