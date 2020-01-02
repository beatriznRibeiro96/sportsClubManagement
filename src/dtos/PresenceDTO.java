package dtos;

import entities.Athlete;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PresenceDTO implements Serializable {

    private String code;
    private String codeTraining;
    private GregorianCalendar dataTraining;
    private List <String> athletesPresentes;
    private String emailCoach;


    public PresenceDTO(String code, String codeTraining, GregorianCalendar dataTraining, String emailCoach) {
        this.code = code;
        this.codeTraining = codeTraining;
        this.dataTraining= dataTraining;
        this.athletesPresentes = new LinkedList<>();
        this.emailCoach = emailCoach;
    }

    public PresenceDTO() {
    }

    public String getCodeTraining() {
        return codeTraining;
    }

    public void setCodeTraining(String codeTraining) {
        this.codeTraining = codeTraining;
    }

    public String getEmailCoach() {
        return emailCoach;
    }

    public void setEmailCoach(String emailCoach) {
        this.emailCoach = emailCoach;
    }

    public GregorianCalendar getDataTraining() {
        return dataTraining;
    }

    public void setDataTraining(GregorianCalendar dataTraining) {
        this.dataTraining = dataTraining;
    }

    public List<String> getAthletesPresentes() {
        return athletesPresentes;
    }

    public void setAthletesPresentes(List <String> athletesPresentes) {
        this.athletesPresentes = athletesPresentes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}