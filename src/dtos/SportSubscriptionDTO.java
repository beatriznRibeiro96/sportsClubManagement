package dtos;

import java.io.Serializable;

public class SportSubscriptionDTO implements Serializable {
    private int code;
    private String name;
    private int rankCode;
    private String rankName;
    private String athleteUsername;
    private String athleteName;

    public SportSubscriptionDTO() {
    }

    public SportSubscriptionDTO(int code, String name, int rankCode, String rankName, String athleteUsername, String athleteName) {
        this.code = code;
        this.name = name;
        this.rankCode = rankCode;
        this.rankName = rankName;
        this.athleteUsername = athleteUsername;
        this.athleteName = athleteName;
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

    public int getRankCode() {
        return rankCode;
    }

    public void setRankCode(int rankCode) {
        this.rankCode = rankCode;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getAthleteUsername() {
        return athleteUsername;
    }

    public void setAthleteUsername(String athleteUsername) {
        this.athleteUsername = athleteUsername;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }
}
