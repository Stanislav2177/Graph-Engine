package org.example.models;

import java.util.List;
import java.util.Map;

public class ShortestPathDto {
    private String start;
    private String target;
    private List<String> shortestPathLabels;
    private List<Integer> shortestPathIds;
    private Map<String, Double> distances;

    public ShortestPathDto(String start, String target, List<String> shortestPathLabels, List<Integer> shortestPathIds, Map<String, Double> distances) {
        this.start = start;
        this.target = target;
        this.shortestPathLabels = shortestPathLabels;
        this.shortestPathIds = shortestPathIds;
        this.distances = distances;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<String> getShortestPathLabels() {
        return shortestPathLabels;
    }

    public void setShortestPathLabels(List<String> shortestPathLabels) {
        this.shortestPathLabels = shortestPathLabels;
    }

    public List<Integer> getShortestPathIds() {
        return shortestPathIds;
    }

    public void setShortestPathIds(List<Integer> shortestPathIds) {
        this.shortestPathIds = shortestPathIds;
    }

    public Map<String, Double> getDistances() {
        return distances;
    }

    public void setDistances(Map<String, Double> distances) {
        this.distances = distances;
    }
}
