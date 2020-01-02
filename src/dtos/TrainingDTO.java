package dtos;

import java.io.Serializable;

public class TrainingDTO implements Serializable {
    private String code;
    private String emailCoach;

    private String codeGrade;

    private Schedule horaInicio;
    private Schedule horaFim;
    private Schedule diaSemana;
    private Collection<PresenceDTO> presences;

    public TrainingDTO() {
    }

    public TrainingDTO(String code, String emailCoach, String codeGrade, Schedule horaInicio, Schedule horaFim, Schedule diaSemana) {
        this.code = code;
        this.emailCoach = emailCoach;

        this.codeGrade = codeGrade;

        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.diaSemana = diaSemana;
        this.presences = new HashSet<>();
    }


    public String getEmailCoach() {
        return emailCoach;
    }

    public void setEmailCoach(String emailCoach) {
        this.emailCoach = emailCoach;
    }



    public String getCodeGrade() {
        return codeGrade;
    }

    public void setCodeGrade(String codeGrade) {
        this.codeGrade = codeGrade;
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

    public void setDiaSemana(Schedule diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Collection<PresenceDTO> getPresences() {
        return presences;
    }

    public void setPresences(Collection<PresenceDTO> presences) {
        this.presences = presences;
    }
}
