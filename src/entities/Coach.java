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
    private Set<Rank> ranks;

    public Coach() {
        this.ranks = new LinkedHashSet<>();
    }

    public Coach(String username, String password, String name, String email, LocalDate birthDate) {
        super(username, password, name, email, birthDate);
        this.ranks = new LinkedHashSet<>();
    }

    public Set<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(Set<Rank> ranks) {
        this.ranks = ranks;
    }

    public void addRank(Rank rank) {
        ranks.add(rank);
    }

    public void removeRank(Rank rank){
        ranks.remove(rank);
    }
}
