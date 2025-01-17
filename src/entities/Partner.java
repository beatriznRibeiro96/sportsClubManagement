package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPartners",
                query = "SELECT p FROM Partner p ORDER BY p.name" // JPQL
        )
})
public class Partner extends User implements Serializable {
    public Partner() {
    }

    public Partner(String username, String password, String name, String email, LocalDate birthDate) {
        super(username, password, name, email, birthDate);
    }
}
