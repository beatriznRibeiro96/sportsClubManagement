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

    @OneToMany(mappedBy = "sport", cascade = CascadeType.REMOVE)
    private Set<Rank> ranks;
    @Version
    private int version;

    public Sport() {
        this.ranks = new LinkedHashSet<>();
    }

    public Sport(int code, String name) {
        this();
        this.code = code;
        this.name = name;
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
