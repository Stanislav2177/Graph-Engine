package org.example.transport;

import org.apache.commons.configuration.ConfigurationException;
import org.example.graph.Node;
import org.example.logger.Logger;
import org.example.models.ScheduleTransportDTO;
import org.example.models.ShortestPathDto;
import org.example.models.TransportVehicle;
import org.example.repository.TransportDataRepo;
import org.example.repository.TransportRoutesRepo;
import org.example.repository.TransportTypesRepo;
import org.example.repository.TransportVehiclesRepo;
import org.example.service.GraphService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

//Main Service which to work directly with all the repos and Workflow needs
public class TransportService {
    //NodesRepo and EdgesRepo functionality will goes through the graphService
    private GraphService graphService;

    //Points from which TransportService will be able to interact with the DB
    private TransportDataRepo transportDataRepo;
    private TransportRoutesRepo transportRoutesRepo;
    private TransportTypesRepo transportTypesRepo;
    private TransportVehiclesRepo transportVehiclesRepo;

    public TransportService() throws ConfigurationException {
        graphService = new GraphService();
        transportDataRepo = new TransportDataRepo();
        transportRoutesRepo = new TransportRoutesRepo();
        transportTypesRepo = new TransportTypesRepo();
        transportVehiclesRepo = new TransportVehiclesRepo();
    }

    public ShortestPathDto findShortestPath(Node source, Node target){
        try{
            ShortestPathDto shortestPath = graphService.findShortestPath(source, target);
            return shortestPath;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System ,"Method findShortestPath failed: " +ex.getMessage());

            return null;
        }
    }

    public double getWeightBetweenTwoNodes(Node source, Node target){
        try{
            double distanceBetweenTwoNodes = graphService.getDistanceBetweenTwoNodes(source.getLabel(), target.getLabel());
            return distanceBetweenTwoNodes;
        }catch (Exception ex){
            return -1;
        }
    }

    public List<ScheduleTransportDTO> getAllSchedulesList(){
        try {
            List<ScheduleTransportDTO> allSchedules = graphService.getAllSchedulesList();

            return allSchedules;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System,"Method startNewTrip failed: " + ex.getMessage());
            return null;
        }
    }

    public int createTransportRoute(List<Integer> routeIds, int sourceId, int targetId){
        try{
            int routeId = transportRoutesRepo.addTransportRoute(routeIds, sourceId, targetId);
            return routeId;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System ,"Method findShortestPath failed: " +ex.getMessage());
            return -1;
        }
    }

    public int createTrip(Trip trip){
        try{
            int i = transportDataRepo.addTransportData(trip.getVehicleId(), trip.getRoutePathId(), trip.getScheduleTime(), trip.getActualDeparture(), trip.getActualArrival(), trip.getTransportDataStatuses());
            return i;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System ,"Method findShortestPath failed: " +ex.getMessage());
            return -1;
        }
    }

    public List<Trip> getAllTrips(){
        return transportDataRepo.getAllTransportData();
    }

    public void getAvailableVehicle(int routePathId){
        try{
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System ,"Method getAvailableVehicle failed: " + ex.getMessage());
        }
    }

    public List<TransportVehicle> getAvailableVehicles(int sourceNodeId, int targetNodeId){
        try{
            List<TransportVehicle> vehiclesByStartAndEndNodeId = transportVehiclesRepo.getVehiclesByStartAndEndNodeId(sourceNodeId, targetNodeId);
            return vehiclesByStartAndEndNodeId;
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.System ,"Method getAvailableVehicle failed: " + ex.getMessage());
            return null;
        }
    }


}
