package org.example.models;

import java.util.Date;
import java.util.List;

public class ScheduleTransportDTO {
    private int sourceNodeId;
    private int targetNodeId;
    private Date dailyEndHour;
    private Date dailyStartHour;
    private boolean scheduleAvailable;
    private List<String> availableDays;

    public ScheduleTransportDTO() {
    }

    public int getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(int sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public int getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(int targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public Date getDailyEndHour() {
        return dailyEndHour;
    }

    public void setDailyEndHour(Date dailyEndHour) {
        this.dailyEndHour = dailyEndHour;
    }

    public Date getDailyStartHour() {
        return dailyStartHour;
    }

    public void setDailyStartHour(Date dailyStartHour) {
        this.dailyStartHour = dailyStartHour;
    }

    public boolean isScheduleAvailable() {
        return scheduleAvailable;
    }

    public void setScheduleAvailable(boolean scheduleAvailable) {
        this.scheduleAvailable = scheduleAvailable;
    }

    public List<String> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays;
    }

    public ScheduleTransportDTO(int sourceNodeId, int targetNodeId, Date dailyEndHour, Date dailyStartHour, boolean scheduleAvailable, List<String> availableDays) {
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
        this.dailyEndHour = dailyEndHour;
        this.dailyStartHour = dailyStartHour;
        this.scheduleAvailable = scheduleAvailable;
        this.availableDays = availableDays;
    }
}
