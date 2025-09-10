package org.example.models;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalTime;

public class ScheduleTransport {
    private String sourceNode;
    private String targetNode;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime  dailyEndHour;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime  dailyStartHour;
    private boolean scheduleAvailable;
    private List<String> availableDays;

    public ScheduleTransport() {
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public LocalTime getDailyEndHour() {
        return dailyEndHour;
    }

    public void setDailyEndHour(LocalTime dailyEndHour) {
        this.dailyEndHour = dailyEndHour;
    }

    public LocalTime getDailyStartHour() {
        return dailyStartHour;
    }

    public void setDailyStartHour(LocalTime dailyStartHour) {
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
}
