package org.example.transport;

public class Transport implements ITransport{
    @Override
    public int vehicleId() {
        return 0;
    }

    @Override
    public String registrationNumber() {
        return null;
    }

    @Override
    public int transportTypeId() {
        return 0;
    }

    @Override
    public int totalSeats() {
        return 0;
    }

    @Override
    public boolean serviceability() {
        return false;
    }

    @Override
    public String additionalInfo() {
        return null;
    }
}
