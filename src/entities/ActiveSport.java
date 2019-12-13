package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ACTIVE_SPORTS")
@NamedQueries({
        @NamedQuery(
                name = "getAllActiveSports",
                query = "SELECT acs FROM ActiveSport acs ORDER BY acs.name"
        )
})
public class ActiveSport implements Serializable {
    @Id
    private int code;
    private String name;

    @ManyToOne
    private Sport sport;

    @ManyToOne
    private Season season;

    public ActiveSport() {
    }

    public ActiveSport(int code, String name, Sport sport, Season season) {
        this.code = code;
        this.name = name;
        this.sport = sport;
        this.season = season;
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

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }
}
