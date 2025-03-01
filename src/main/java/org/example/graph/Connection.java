package org.example.graph;

public class Connection {
    Node start;
    double weight;
    Node end;


    public Connection(Node start, double weight, Node end) {
        this.start = start;
        this.weight = weight;
        this.end = end;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }
}
