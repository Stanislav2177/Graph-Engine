package org.example.graph;

import org.example.graph.Node;

//class to store edges of the weighted graph
public class Edge {

        Node dest;
        double weight;
        public Edge(Node dest, double weight) {
            this.dest = dest;
            this.weight = weight;
        }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
