package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class RankDTO implements Serializable {
    private int code;
    private String name;
    private int idadeMin;
    private int idadeMax;
    private int activeSportCode;
    private String activeSportName;
    private Collection<SportSubscriptionDTO> sportSubscriptions;
    private Collection<CoachDTO> coaches;
    private Collection<ScheduleDTO> schedules;
    private Collection<TrainingDTO> trainings;

    public RankDTO() {
        this.coaches = new LinkedHashSet<>();
        this.sportSubscriptions = new LinkedHashSet<>();
        this.schedules = new LinkedHashSet<>();
        this.trainings = new LinkedHashSet<>();
    }

    public RankDTO(int code, String name, int idadeMin, int idadeMax, int activeSportCode, String activeSportName) {
        this.code = code;
        this.name = name;
        this.idadeMin = idadeMin;
        this.idadeMax = idadeMax;
        this.activeSportCode = activeSportCode;
        this.activeSportName = activeSportName;
        this.coaches = new LinkedHashSet<>();
        this.sportSubscriptions = new LinkedHashSet<>();
        this.schedules = new LinkedHashSet<>();
        this.trainings = new LinkedHashSet<>();
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

    public int getActiveSportCode() {
        return activeSportCode;
    }

    public void setActiveSportCode(int activeSportCode) {
        this.activeSportCode = activeSportCode;
    }

    public String getActiveSportName() {
        return activeSportName;
    }

    public void setActiveSportName(String activeSportName) {
        this.activeSportName = activeSportName;
    }

    public Collection<SportSubscriptionDTO> getSportSubscriptions() {
        return sportSubscriptions;
    }

    public void setSportSubscriptions(Collection<SportSubscriptionDTO> sportSubscriptions) {
        this.sportSubscriptions = sportSubscriptions;
    }

    public Collection<CoachDTO> getCoaches() {
        return coaches;
    }

    public void setCoaches(Collection<CoachDTO> coaches) {
        this.coaches = coaches;
    }

    public Collection<ScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(Collection<ScheduleDTO> schedules) {
        this.schedules = schedules;
    }

    public Collection<TrainingDTO> getTrainings() {
        return trainings;
    }

    public void setTrainings(Collection<TrainingDTO> trainings) {
        this.trainings = trainings;
    }
}
