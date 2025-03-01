package org.example.graph;

import java.util.List;

public class EdgeData {
    private String source;
    private List<String> targets;
    private String weight;

    public EdgeData(String source, List<String> targets, String weight) {
        this.source = source;
        this.targets = targets;
        this.weight = weight;
    }

    public EdgeData() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "EdgeData{" +
                "source='" + source + '\'' +
                ", targets=" + targets +
                ", weight='" + weight + '\'' +
                '}';
    }
}