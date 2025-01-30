package org.example.graph;

//class to store edges of the weighted graph
public class Edge {

        Node node;
        double weight;
        public Edge(Node dest, double weight) {
            this.node = dest;
            this.weight = weight;
        }

    public Edge() {
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
