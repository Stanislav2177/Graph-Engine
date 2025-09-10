package org.example.graph;

public class ConnectionDB {
    private int sourceNode_id;
    private int targetNode_id;
    private double weight;

    public ConnectionDB(int sourceNode_id, int targetNode_id, double weight) {
        this.sourceNode_id = sourceNode_id;
        this.targetNode_id = targetNode_id;
        this.weight = weight;
    }

    public ConnectionDB(int targetNode_id, double weight) {
        this.targetNode_id = targetNode_id;
        this.weight = weight;
    }



    public int getSourceNode_id() {
        return sourceNode_id;
    }

    public void setSourceNode_id(int sourceNode_id) {
        this.sourceNode_id = sourceNode_id;
    }

    public int getTargetNode_id() {
        return targetNode_id;
    }

    public void setTargetNode_id(int targetNode_id) {
        this.targetNode_id = targetNode_id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
