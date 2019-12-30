package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class GradeDTO implements Serializable {
    private int code;
    private String name;
    private int activeSportCode;
    private String activeSportName;
    private Collection<AthleteDTO> athletes;

    public GradeDTO() {
        this.athletes = new LinkedHashSet<>();
    }

    public GradeDTO(int code, String name, int activeSportCode, String activeSportName) {
        this.code = code;
        this.name = name;
        this.activeSportCode = activeSportCode;
        this.activeSportName = activeSportName;
        this.athletes = new LinkedHashSet<>();
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

    public Collection<AthleteDTO> getAthletes() {
        return athletes;
    }

    public void setAthletes(Collection<AthleteDTO> athletes) {
        this.athletes = athletes;
    }
}
