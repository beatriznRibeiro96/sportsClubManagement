package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="SEASONS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllSeasons",
                query = "SELECT sea FROM Season sea ORDER BY sea.name"
        ),
        @NamedQuery(
                name = "countSeasonByName",
                query = "SELECT count(sea) FROM Season sea WHERE sea.name = :name"
        )
})
public class Season implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotNull(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @Version
    private int version;

    public Season() {
    }

    public Season(String name) {
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
