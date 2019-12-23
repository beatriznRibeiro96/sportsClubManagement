package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="SPORT_SUBSCRIPTIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"RANK_CODE", "ATHLETE_USERNAME"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllSportSubscriptions",
                query = "SELECT ss FROM SportSubscription ss ORDER BY ss.code"
        ),
        @NamedQuery(
                name = "countSportSubscriptionByRankAndAthlete",
                query = "SELECT count(ss) FROM SportSubscription ss WHERE ss.rank = :rank AND ss.athlete = :athlete"
        )
})
public class SportSubscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "rank is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private Rank rank;
    @NotNull(message = "athlete is mandatory")
    @JoinColumn(nullable = false)
    @ManyToOne
    private Athlete athlete;
    @Version
    private int version;

    public SportSubscription() {
    }

    public SportSubscription(String name, Rank rank, Athlete athlete) {
        this.name = name;
        this.rank = rank;
        this.athlete = athlete;
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }
}
