package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="SPORTS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllSports",
                query = "SELECT s FROM Sport s ORDER BY s.name"
        ),
        @NamedQuery(
                name = "countSportByName",
                query = "SELECT count(s) FROM Sport s WHERE s.name = :name"
        )
})
public class Sport implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotNull(message = "name is mandatory")
    @Column(nullable = false)
    private String name;

    @Version
    private int version;

    public Sport() {
    }

    public Sport(String name) {
        this();
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
}
