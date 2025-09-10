package org.example.transport;

import java.time.LocalDateTime;

public class DetailedTransportData {
    private int transportDataId;
    private LocalDateTime scheduleTime;
    private LocalDateTime actualDeparture;
    private LocalDateTime actualArrival;
    private String currentStatus;

    private int vehicleId;
    private String registrationNumber;
    private int totalSeats;
    private String engineInfo;
    private boolean serviceability;
    private String additionalInfo;

    private String transportTypeName;
    private String transportTypeDescription;

    private int routeId;
    private String routePath;
    private boolean isStarted;
    private String problem;

    private int sourceNodeId;
    private String sourceLabel;
    private double sourceLat;
    private double sourceLng;

    private int targetNodeId;
    private String targetLabel;
    private double targetLat;
    private double targetLng;

    public DetailedTransportData() {
    }

    public DetailedTransportData(int transportDataId, LocalDateTime scheduleTime, LocalDateTime actualDeparture, LocalDateTime actualArrival, String currentStatus, int vehicleId, String registrationNumber, int totalSeats, String engineInfo, boolean serviceability, String additionalInfo, String transportTypeName, String transportTypeDescription, int routeId, String routePath, boolean isStarted, String problem, int sourceNodeId, String sourceLabel, double sourceLat, double sourceLng, int targetNodeId, String targetLabel, double targetLat, double targetLng) {
        this.transportDataId = transportDataId;
        this.scheduleTime = scheduleTime;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
        this.currentStatus = currentStatus;
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.totalSeats = totalSeats;
        this.engineInfo = engineInfo;
        this.serviceability = serviceability;
        this.additionalInfo = additionalInfo;
        this.transportTypeName = transportTypeName;
        this.transportTypeDescription = transportTypeDescription;
        this.routeId = routeId;
        this.routePath = routePath;
        this.isStarted = isStarted;
        this.problem = problem;
        this.sourceNodeId = sourceNodeId;
        this.sourceLabel = sourceLabel;
        this.sourceLat = sourceLat;
        this.sourceLng = sourceLng;
        this.targetNodeId = targetNodeId;
        this.targetLabel = targetLabel;
        this.targetLat = targetLat;
        this.targetLng = targetLng;
    }

    public int getTransportDataId() {
        return transportDataId;
    }

    public void setTransportDataId(int transportDataId) {
        this.transportDataId = transportDataId;
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

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getEngineInfo() {
        return engineInfo;
    }

    public void setEngineInfo(String engineInfo) {
        this.engineInfo = engineInfo;
    }

    public boolean isServiceability() {
        return serviceability;
    }

    public void setServiceability(boolean serviceability) {
        this.serviceability = serviceability;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getTransportTypeName() {
        return transportTypeName;
    }

    public void setTransportTypeName(String transportTypeName) {
        this.transportTypeName = transportTypeName;
    }

    public String getTransportTypeDescription() {
        return transportTypeDescription;
    }

    public void setTransportTypeDescription(String transportTypeDescription) {
        this.transportTypeDescription = transportTypeDescription;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public int getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(int sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSourceLng() {
        return sourceLng;
    }

    public void setSourceLng(double sourceLng) {
        this.sourceLng = sourceLng;
    }

    public int getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(int targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public String getTargetLabel() {
        return targetLabel;
    }

    public void setTargetLabel(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    public double getTargetLat() {
        return targetLat;
    }

    public void setTargetLat(double targetLat) {
        this.targetLat = targetLat;
    }

    public double getTargetLng() {
        return targetLng;
    }

    public void setTargetLng(double targetLng) {
        this.targetLng = targetLng;
    }

    // Getters and Setters (or use Lombok if allowed)
}