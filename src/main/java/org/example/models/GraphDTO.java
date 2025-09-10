package org.example.models;


//For Edges presented in the json
public class GraphDTO {
    private String id;
    private String source;
    private String target;
    private double weight;

    public GraphDTO() {}

    public GraphDTO(String id, String source, String target, double weight) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
