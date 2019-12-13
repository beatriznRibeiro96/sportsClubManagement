package dtos;

import java.io.Serializable;

public class SportSubscriptionDTO implements Serializable {
    private int code;
    private int activeSportCode;
    private String activeSportName;
    private String athleteUsername;

    public SportSubscriptionDTO() {
    }

    public SportSubscriptionDTO(int code, int activeSportCode, String activeSportName, String athleteUsername) {
        this.code = code;
        this.activeSportCode = activeSportCode;
        this.activeSportName = activeSportName;
        this.athleteUsername = athleteUsername;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public String getAthleteUsername() {
        return athleteUsername;
    }

    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }
}
