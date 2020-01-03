package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@MappedSuperclass
@Entity
@Table(name = "TRAININGS")
@NamedQueries({
        @NamedQuery(
                name = "getAllTrainings",
                query = "SELECT t FROM Training t ORDER BY p.id"
        )
})
public class Training {

    @Id
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Training_Coach")
    private Coach coach;

    @NotNull
    @ManyToOne
    private Grade grade;

    @NotNull
    private Schedule horaInicio;

    @NotNull
    private Schedule horaFim;

    @NotNull
    private Schedule diaSemana;

    @OneToMany(mappedBy = "training", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Presence> presences;

    public Product() {
        this.presences = new HashSet<>();
    }

    public Training(String code, Coach coach, Grade grade, Schedule horaInicio, Schedule horaFim, Schedule diaSemana) {
        this.code = code;
        this.coach = coach;

        this.grade = grade;

        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.diaSemana = diaSemana;
        this.presences = new HashSet<>();
    }

    public Schedule getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Schedule horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Schedule getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Schedule horaFim) {
        this.horaFim = horaFim;
    }

    public Schedule getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Schedule dia) {
        this.diaSemana = dia;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }



    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }



    public Set<Presence> getPresences() {
        return presences;
    }

    public void addPresence(Presence presence) {
        if (!presences.contains(presence)) {
            presences.add(presence);
        }
    }

    public void removePresence(Presence presence) {
        presences.remove(presence);
    }
}