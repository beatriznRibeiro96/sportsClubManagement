package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "RANKS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "SPORT_CODE"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllRanks",
                query = "SELECT r FROM Rank r ORDER BY r.name"
        )
})
public class Rank implements Serializable {
    @Id
    private int code;
    private String name;
    @ManyToOne
    @JoinColumn(name="SPORT_CODE", nullable = false)
    private Sport sport;

    public Rank() {
    }

    public Rank(int code, String name, Sport sport) {
        this.code = code;
        this.name = name;
        this.sport = sport;
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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
