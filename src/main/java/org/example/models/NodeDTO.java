package org.example.models;


//For nodes which are presented in the json
public class NodeDTO {
    private String id;
    private String label;

    private double lat;

    private double lng;

    public NodeDTO(String id, String label, double lat, double lng) {
        this.id = id;
        this.label = label;
        this.lat = lat;
        this.lng = lng;
    }

    public NodeDTO(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
