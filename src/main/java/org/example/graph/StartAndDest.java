package org.example.graph;

public class StartAndDest {

    private String source;
    private String target;


    public StartAndDest() {
    }

    public StartAndDest(String source, String target) {
        this.source = source;
        this.target = target;
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
}
