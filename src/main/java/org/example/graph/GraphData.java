package org.example.graph;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


//Main  model for deserialization of the JSON received from the admin console
public class GraphData {
    @JsonProperty("source")
    private String source;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("isStation")
    private boolean isStation;

    @JsonProperty("target")
    private List<Edge> target;

    public GraphData() {}

    public GraphData(String source, String longitude, String latitude, boolean isStation, List<Edge> target) {
        this.source = source;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isStation = isStation;
        this.target = target;
    }

    public GraphData(String source, List<Edge> destinations) {
        this.source = source;
        this.target = destinations;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public List<Edge> getTarget() {
        return target;
    }

    public void setTarget(List<Edge> target) {
        this.target = target;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public boolean isStation() {
        return isStation;
    }

    public void setStation(boolean station) {
        isStation = station;
    }
}