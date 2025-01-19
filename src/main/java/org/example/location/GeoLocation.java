package org.example.location;

public class GeoLocation implements Location{

    public String locationOnMap;
    public int ID;


    public GeoLocation(String locationOnMap, int ID) {
        this.locationOnMap = locationOnMap;
        this.ID = ID;
    }

    @Override
    public String location() {
        return this.locationOnMap;
    }

    @Override
    public int ID() {
        return this.ID;
    }


}
