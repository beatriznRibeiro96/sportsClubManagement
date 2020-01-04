package dtos;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;

public class TrainingDTO implements Serializable {
    private int code;
    private String name;
    private int rankCode;
    private String rankName;
    private int scheduleCode;
    private String scheduleName;
    private Collection<AthleteDTO> presences;

    public TrainingDTO() {
        this.presences = new LinkedHashSet<>();
    }

    public TrainingDTO(int code, String name, int rankCode, String rankName, int scheduleCode, String scheduleName) {
        this.code = code;
        this.name = name;
        this.rankCode = rankCode;
        this.rankName = rankName;
        this.scheduleCode = scheduleCode;
        this.scheduleName = scheduleName;
        this.presences = new LinkedHashSet<>();
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

    public int getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(int scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Collection<AthleteDTO> getPresences() {
        return presences;
    }

    public void setPresences(Collection<AthleteDTO> presences) {
        this.presences = presences;
    }
}
