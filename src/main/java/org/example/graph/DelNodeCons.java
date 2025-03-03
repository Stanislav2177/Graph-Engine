package org.example.graph;

public class DelNodeCons {

    private String id;
    private String source;
    private String target;
    private int weight;

    public DelNodeCons(String id, String source, String target, int weight) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public DelNodeCons() {}

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
