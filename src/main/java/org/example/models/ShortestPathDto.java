package org.example.models;

import java.util.List;
import java.util.Map;

public class ShortestPathDto {
    private String start;
    private String target;
    private List<String> shortestPath;
    private Map<String, Double> distances;

    public ShortestPathDto(String start, String target, List<String> shortestPath, Map<String, Double> distances) {
        this.start = start;
        this.target = target;
        this.shortestPath = shortestPath;
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

    public List<String> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<String> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<String, Double> getDistances() {
        return distances;
    }

    public void setDistances(Map<String, Double> distances) {
        this.distances = distances;
    }
}
