package dtos;

import java.io.Serializable;

public class ScheduleDTO implements Serializable {
    private int code;
    private String name;
    private int dayOfWeekCode;
    private String dayOfWeekName;
    private String startTime;
    private String endTime;
    private int activeSportCode;
    private String activeSportName;

    public ScheduleDTO() {
    }

    public ScheduleDTO(int code, String name, int dayOfWeekCode, String dayOfWeekName, String startTime, String endTime, int activeSportCode, String activeSportName) {
        this.code = code;
        this.name = name;
        this.dayOfWeekCode = dayOfWeekCode;
        this.dayOfWeekName = dayOfWeekName;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public int getDayOfWeekCode() {
        return dayOfWeekCode;
    }

    public void setDayOfWeekCode(int dayOfWeekCode) {
        this.dayOfWeekCode = dayOfWeekCode;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public void setDayOfWeekName(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
