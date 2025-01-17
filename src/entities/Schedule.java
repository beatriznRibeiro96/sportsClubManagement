package entities;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="SCHEDULES", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME", "RANK_CODE"}))
@NamedQueries({
        @NamedQuery(
                name = "getAllSchedules",
                query = "SELECT sch FROM Schedule sch ORDER BY sch.name"
        ),
        @NamedQuery(
                name = "countSchedulesByNameAndRank",
                query = "SELECT count(sch) FROM Schedule sch WHERE sch.name = :name AND sch.rank = :rank"
        )
})
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int code;
    @NotBlank(message = "name is mandatory")
    @Column(nullable = false)
    private String name;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne
    private Rank rank;
    @Version
    private int version;

    public Schedule() {
    }

    public Schedule(String name, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Rank rank) {
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rank = rank;
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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public long duration(){
        return ChronoUnit.MINUTES.between(this.startTime, this.endTime);
    }
}
