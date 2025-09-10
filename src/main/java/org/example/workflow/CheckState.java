package org.example.workflow;

import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.models.ShortestPathDto;
import org.example.transport.Transport;
import org.example.transport.TransportService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CheckState implements State{
    private TransportService transportService;

    public CheckState() throws ConfigurationException {
        transportService = new TransportService();
    }

    //Looking for route from point A to point B
    //Trying to get the shortest path
    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) {
        try {
            ShortestPathDto shortestPathDto = transportService.findShortestPath(transferModel.getSource(), transferModel.getTarget());
            List<String> shortestPathLabes = shortestPathDto.getShortestPathLabels();
            List<Integer> shortestPathIntegers = shortestPathDto.getShortestPathIds();

            if(shortestPathLabes.size() > 0){
                Collection<Double> values = shortestPathDto.getDistances().values();
                double total = 1;
                for (Double value : values) {
                    total += value.doubleValue();
                }
                //Set the route between the two nodes
                transferModel.setRouteLabels(shortestPathLabes);
                transferModel.setRouteIds(shortestPathIntegers);
                transferModel.setTotalWeight(total);
                Set<String> stringSet = shortestPathDto.getDistances().keySet();
                workflow.changeState(new CreateRoutePathState());

                Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Found ShortestPaht with size: " + stringSet.toString() + " and distances " + total);
            }else{
                transferModel.setResponseInfo("Двете станции нямат връзка между тях");
                workflow.changeState(new FailedState());

            }
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Something terrible happened in state CheckState: " + ex.getMessage());
            workflow.changeState(new FailedState());
        }
    }
}
