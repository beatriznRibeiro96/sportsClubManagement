package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "METHOD_PAYMENTS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"METHOD"})
)
@NamedQueries(
        @NamedQuery(
                name = "getAllMethods",
                query = "SELECT m FROM MethodPayment m ORDER BY m.method"
        )
)
public class MethodPayment {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private String method;

    @NotNull
    private  boolean invalid = false;

    public MethodPayment() {

    }

    public MethodPayment(String method) {
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
}
