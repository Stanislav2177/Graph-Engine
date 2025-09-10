package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class TripDataDTO {
    @JsonProperty("source")
    private String source;

    @JsonProperty("target")
    private String target;

    @JsonProperty("startHour")
    private Date startHour;

    @JsonProperty("endHour")
    private Date endHour;

    private List<String> availableDays;

    public TripDataDTO() {
    }

    public TripDataDTO(String source, String target, Date startHour, Date endHour, List<String> availableDays) {
        this.source = source;
        this.target = target;
        this.startHour = startHour;
        this.endHour = endHour;
        this.availableDays = availableDays;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public Date getEndHour() {
        return endHour;
    }

    public void setEndHour(Date endHour) {
        this.endHour = endHour;
    }

    public List<String> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(List<String> availableDays) {
        this.availableDays = availableDays;
    }
}
