package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT a FROM Administrator a ORDER BY a.name" // JPQL
        )
})
public class Administrator extends User implements Serializable {
    public Administrator() {
    }

    public Administrator(String username, String password, String name, String email, LocalDate birthDate) {
        super(username, password, name, email, birthDate);
    }
}