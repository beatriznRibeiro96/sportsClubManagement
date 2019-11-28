package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllCoaches",
                query = "SELECT c FROM Coach c ORDER BY c.name" // JPQL
        )
})
public class Coach extends User implements Serializable {
    public Coach() {
    }

    public Coach(String username, String password, String name, String email) {
        super(username, password, name, email);
    }
}
