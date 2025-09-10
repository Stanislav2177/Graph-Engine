package org.example.workflow;

import org.example.graph.Node;
import org.example.models.ApiResponse;
import org.example.models.TransportVehicle;
import org.example.models.TripDataDTO;

import java.util.List;

public class StatesTransferModel {
    private boolean isSuccessful;
    private Node source;
    private Node target;
    private String transportRegNumber;
    private int DB_routePathId;
    private String responseInfo;

    private TripDataDTO tripDataDTO;

    //Data is filled after the check state

    private List<String> routeLabels;
    private List<Integer> routeIds;
    //Data is filled after CheckForVehicleState

    private TransportVehicle vehicle;

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    private double totalWeight;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(String responseInfo) {
        if(!responseInfo.isEmpty()){
            this.responseInfo += "\n" + responseInfo + ";\n";
        }
    }

    public int getDB_routePathId() {
        return DB_routePathId;
    }

    public TransportVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(TransportVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public TripDataDTO getTripDataDTO() {
        return tripDataDTO;
    }

    public void setTripDataDTO(TripDataDTO tripDataDTO) {
        this.tripDataDTO = tripDataDTO;
    }

    public void setDB_routePathId(int DB_routePathId) {
        this.DB_routePathId = DB_routePathId;
    }

    public List<String> getRouteLabels() {
        return routeLabels;
    }

    public void setRouteLabels(List<String> routeLabels) {
        this.routeLabels = routeLabels;
    }

    public List<Integer> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<Integer> routeIds) {
        this.routeIds = routeIds;
    }

    public StatesTransferModel() {
    }

    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public String getTransportRegNumber() {
        return transportRegNumber;
    }

    public void setTransportRegNumber(String transportRegNumber) {
        this.transportRegNumber = transportRegNumber;
    }
}
