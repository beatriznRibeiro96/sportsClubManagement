package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class ActiveSportDTO implements Serializable {
    private int code;
    private String name;
    private int sportCode;
    private String sportName;
    private int seasonCode;
    private String seasonName;
    private Collection<CoachDTO> coaches;

    public ActiveSportDTO() {
        this.coaches = new LinkedHashSet<>();
    }

    public ActiveSportDTO(int code, String name, int sportCode, String sportName, int seasonCode, String seasonName) {
        this.code = code;
        this.name = name;
        this.sportCode = sportCode;
        this.sportName = sportName;
        this.seasonCode = seasonCode;
        this.seasonName = seasonName;
        this.coaches = new LinkedHashSet<>();
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

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getSportCode() {
        return sportCode;
    }

    public void setSportCode(int sportCode) {
        this.sportCode = sportCode;
    }

    public int getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonCode(int seasonCode) {
        this.seasonCode = seasonCode;
    }

    public Collection<CoachDTO> getCoaches() {
        return coaches;
    }

    public void setCoaches(Collection<CoachDTO> coaches) {
        this.coaches = coaches;
    }
}