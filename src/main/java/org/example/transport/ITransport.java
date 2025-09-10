package org.example.transport;

public interface ITransport {
    int vehicleId();
    String registrationNumber();
    int transportTypeId();
    int totalSeats();
    boolean serviceability();
    String additionalInfo();
}
