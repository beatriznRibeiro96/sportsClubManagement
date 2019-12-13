package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="SEASONS")
@NamedQueries({
        @NamedQuery(
                name = "getAllSeasons",
                query = "SELECT sea FROM Season sea ORDER BY sea.name"
        )
})
public class Season implements Serializable {
    @Id
    private int code;
    @NotNull
    @Column(nullable = false)
    private String name;

    public Season() {
    }

    public Season(int code, String name) {
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
}
