package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;


@Entity
@Table(name = "PRESENCES")
@NamedQueries(
        @NamedQuery(
                name = "getAllPresences",
                query = "SELECT p FROM Presence p ORDER BY p.code"
        )
)
public class Presence {

    @Id
    private String code;

    @NotNull
    @ManyToOne
    private Training training;

    @NotNull
    private GregorianCalendar dataTraining;

    @NotNull
    @ManyToMany
    private List<Athlete> athletesPresentes;

    @NotNull
    @ManyToOne
    private Coach coach;

    public Presence() {
    }

    public Presence(String code, Training training, GregorianCalendar dataTraining, Coach coach){
        this.code = code;
        this.training = training;
        this.dataTraining = dataTraining;
        this.coach = coach;
        this.athletesPresentes = new LinkedList<>();
    }

    public  getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.Training = Training;
    }

    public GregorianCalendar getDataTraining() {
        return dataTraining;
    }

    public void setDataTraining(GregorianCalendar dataTraining) {
        this.dataTraining = dataTraining;
    }

    public List<Athlete> getathletesPresentes() {
        return athletesPresentes;
    }

    public void setathletesPresentes(List<Athlete> athletesPresentes) {
        this.athletesPresentes = athletesPresentes;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void addAthletePresente (Athlete athlete){
        if (!athletesPresentes.contains(athlete)){
            athletesPresentes.add(athlete);
        }
    }

    public void removeAthletePresente (Athlete athlete){
        if (athletesPresentes.contains(athlete)){
            athletesPresentes.remove(athlete);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}