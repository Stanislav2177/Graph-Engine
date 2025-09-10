package org.example.transport;

import org.example.models.TransportDataStatuses;

import java.time.LocalDateTime;

public class Trip {
    private int tripId;
    private int vehicleId;
    private int routePathId;
    private LocalDateTime scheduleTime;
    private LocalDateTime actualDeparture;
    private LocalDateTime actualArrival;
    private TransportDataStatuses transportDataStatuses;
    private double progress;
    private TripStatus status;


    public Trip(int vehicleId, int routePathId, LocalDateTime scheduleTime, LocalDateTime actualDeparture, LocalDateTime actualArrival, TransportDataStatuses transportDataStatuses) {
        this.vehicleId = vehicleId;
        this.routePathId = routePathId;
        this.scheduleTime = scheduleTime;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
        this.transportDataStatuses = transportDataStatuses;
        progress = 0;
    }

    public Trip(int tripId, int vehicleId, int routePathId, LocalDateTime scheduleTime, LocalDateTime actualDeparture, LocalDateTime actualArrival, TransportDataStatuses transportDataStatuses, double progress, TripStatus status) {
        this.tripId = tripId;
        this.vehicleId = vehicleId;
        this.routePathId = routePathId;
        this.scheduleTime = scheduleTime;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
        this.transportDataStatuses = transportDataStatuses;
        this.progress = progress;
        this.status = status;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getRoutePathId() {
        return routePathId;
    }

    public void setRoutePathId(int routePathId) {
        this.routePathId = routePathId;
    }

    public LocalDateTime getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
    }

    public TransportDataStatuses getTransportDataStatuses() {
        return transportDataStatuses;
    }

    public void setTransportDataStatuses(TransportDataStatuses transportDataStatuses) {
        this.transportDataStatuses = transportDataStatuses;
    }
}
