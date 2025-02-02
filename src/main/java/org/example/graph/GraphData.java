package org.example.graph;

import java.util.List;

public class GraphData {
    private String source;
    private List<Edge> target;

    public GraphData() {}

    public GraphData(String source, List<Edge> destinations) {
        this.source = source;
        this.target = destinations;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public List<Edge> getTarget() {
        return target;
    }

    public void setTarget(List<Edge> target) {
        this.target = target;
    }
}