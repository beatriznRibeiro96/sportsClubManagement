package dtos;

import java.io.Serializable;

public class RankDTO implements Serializable {
    private int code;
    private String name;
    private SportDTO sport;

    public RankDTO() {
    }

    public RankDTO(int code, String name, SportDTO sport) {
        this.code = code;
        this.name = name;
        this.sport = sport;
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

    public SportDTO getSport() {
        return sport;
    }

    public void setSport(SportDTO sport) {
        this.sport = sport;
    }
}
