package org.example.graph;

import java.util.List;

public class GraphData {
    private Node source;
    private List<Edge> destinations;

    public GraphData() {}

    public GraphData(Node source, List<Edge> destinations) {
        this.source = source;
        this.destinations = destinations;
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public List<Edge> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Edge> destinations) {
        this.destinations = destinations;
    }
}