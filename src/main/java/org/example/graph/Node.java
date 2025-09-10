package org.example.graph;

import java.util.Objects;

public class Node {
    private String label;
    private int id;
    private boolean isStation;
    private double lat;
    private double lng;

    public Node(int id, String label, boolean isStation, double lat, double lng) {
        this.label = label;
        this.id = id;
        this.isStation = isStation;
        this.lat = lat;
        this.lng = lng;
    }

    public Node(String label, double lat, double lng) {
        this.label = label;
        this.lat = lat;
        this.lng = lng;
        this.isStation = false;
        this.id = -1;
    }

    public Node(String label){
        this.label = label;
        isStation = false;
    }

    public Node(int id){
        this.id = id;
    }

    public boolean isStation() {
        return isStation;
    }

    public void setStation(boolean station) {
        isStation = station;
    }
    public Node() {
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return Objects.equals(label, node.label);
    }
    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return "Node{label='" + label + "'}";
    }
}
