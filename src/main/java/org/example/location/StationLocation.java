package org.example.location;

public class StationLocation implements Location{

    public String location;
    public int ID;
    @Override
    public String location() {
        return this.location;
    }

    @Override
    public int ID() {
        return this.ID;
    }
}
