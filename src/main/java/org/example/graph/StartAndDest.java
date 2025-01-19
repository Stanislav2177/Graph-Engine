package org.example.graph;

public class StartAndDest {
    private int id;
    private String source;
    private String target;

    private double weight;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public StartAndDest() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return source;
    }

    public void setSource(String start) {
        this.source = start;
    }

    public String getSource() {
        return target;
    }

    public void setTarget(String dest) {
        this.target = dest;
    }
}
