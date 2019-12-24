package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="RANKS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "ACTIVESPORT_CODE"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllRanks",
                query = "SELECT r FROM Rank r ORDER BY r.name"
        ),
        @NamedQuery(
                name = "countRanksByNameAndActiveSport",
                query = "SELECT count(r) FROM Rank r WHERE r.name = :name AND r.activeSport = :activeSport"
        )
})
public class Rank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "minimum age is mandatory")
    @Column(nullable = false)
    private int idadeMin;
    @NotNull(message = "maximum age is mandatory")
    @Column(nullable = false)
    private int idadeMax;
    @NotNull(message = "active sport is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private ActiveSport activeSport;

    @Version
    private int version;

    public Rank() {
    }

    public Rank(String name, int idadeMin, int idadeMax, ActiveSport activeSport) {
        this.name = name;
        this.idadeMin = idadeMin;
        this.idadeMax = idadeMax;
        this.activeSport = activeSport;
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

    public int getIdadeMin() {
        return idadeMin;
    }

    public void setIdadeMin(int idadeMin) {
        this.idadeMin = idadeMin;
    }

    public int getIdadeMax() {
        return idadeMax;
    }

    public void setIdadeMax(int idadeMax) {
        this.idadeMax = idadeMax;
    }

    public ActiveSport getActiveSport() {
        return activeSport;
    }

    public void setActiveSport(ActiveSport activeSport) {
        this.activeSport = activeSport;
    }
}
