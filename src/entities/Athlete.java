package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAthletes",
                query = "SELECT at FROM Athlete at ORDER BY at.name" // JPQL
        )
})
public class Athlete extends Partner implements Serializable {
    public Athlete() {
    }

    public Athlete(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
