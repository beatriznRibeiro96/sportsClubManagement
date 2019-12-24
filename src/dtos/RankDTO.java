package dtos;

import java.io.Serializable;

public class RankDTO implements Serializable {
    private int code;
    private String name;
    private int idadeMin;
    private int idadeMax;
    private int activeSportCode;
    private String activeSportName;

    public RankDTO() {
    }

    public RankDTO(int code, String name, int idadeMin, int idadeMax, int activeSportCode, String activeSportName) {
        this.code = code;
        this.name = name;
        this.idadeMin = idadeMin;
        this.idadeMax = idadeMax;
        this.activeSportCode = activeSportCode;
        this.activeSportName = activeSportName;
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
}
