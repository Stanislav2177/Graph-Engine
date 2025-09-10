package org.example.graph;

import java.util.List;

public class EdgeData {
    private String source;
    private String target;
    private String weight;

    public EdgeData(String source, String target, String weight) {
        this.source = source;
        this.target = target;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String targets) {
        this.target = targets;
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
                ", targets=" + target +
                ", weight='" + weight + '\'' +
                '}';
    }
}