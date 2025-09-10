package org.example.models;

public class TransportVehicleDetailedDTO {
    private int vehicleId;
    private String registrationNumber;
    private int totalSeats;
    private String engineInfo;
    private boolean serviceability;
    private String additionalInfo;

    private int targetNodeId;
    private String targetLabel;

    private int sourceNodeId;
    private String sourceLabel;

    public TransportVehicleDetailedDTO(int vehicleId, String registrationNumber, int totalSeats, String engineInfo, boolean serviceability, String additionalInfo, int targetNodeId, String targetLabel, int sourceNodeId, String sourceLabel) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.totalSeats = totalSeats;
        this.engineInfo = engineInfo;
        this.serviceability = serviceability;
        this.additionalInfo = additionalInfo;
        this.targetNodeId = targetNodeId;
        this.targetLabel = targetLabel;
        this.sourceNodeId = sourceNodeId;
        this.sourceLabel = sourceLabel;
    }

    public TransportVehicleDetailedDTO() {
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
}
