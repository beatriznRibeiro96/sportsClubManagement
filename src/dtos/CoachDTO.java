package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class CoachDTO extends UserDTO implements Serializable {
    /*private String sport;
    private String scale;
    private String schedules;*/
    //private Collection<AthleteDTO> athletes;

    public CoachDTO() {
        //this.athletes = new LinkedHashSet<>();
    }

    public CoachDTO(String username, String password, String name, String email /*, String sport, String scale, String schedules*/) {
        super(username, password, name, email);
        //this.sport = sport;
        //this.scale = scale;
        //this.schedules = schedules;
        //this.athletes = new LinkedHashSet<>();
    }

    @Override
    public void reset() {
        super.reset();
        // setAthletes(null);
        // setSport(null);
        //setScale(null);
        //setSchedules(null);

    }

    /*public String getSport() {
        return sport;
    }
    public void setSport(String sport) {
        this.sport = sport;
    }
    public String getScale() {
        return scale;
    }
    public void setScale(String scale) {
        this.scale = scale;
    }
    public String getSchedules() {
        return schedules;
    }
    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }
    /*public Collection<AthleteDTO> getAthletes() {
        return athelets;
    }
    public void setAthletes(Collection<AthleteDTO> athletes) {
        this.athletes = athletes;
    }*/
}
