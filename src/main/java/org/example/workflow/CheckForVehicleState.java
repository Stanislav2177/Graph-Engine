package org.example.workflow;

import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.models.TransportVehicle;
import org.example.repository.TransportVehiclesRepo;

import java.util.List;
import java.util.Map;

public class CheckForVehicleState implements State{
    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) {
        try{
            if(transferModel.getDB_routePathId() != -1){
                List<TransportVehicle> availableVehicles = workflow.getTransportService().getAvailableVehicles(transferModel.getSource().getId(), transferModel.getTarget().getId());

                if(availableVehicles.size() > 0){
                    TransportVehicle transportVehicle = availableVehicles.get(0);
                    transferModel.setVehicle(transportVehicle);
                    workflow.changeState(new CheckFromAvailableScheduleTime());
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Transport Vehicle is found with registration number: "
                            + transportVehicle.getRegistrationNumber() + " and type: " + transportVehicle.getTransportType().getDescription());
                }else{
                    transferModel.setSuccessful(false);
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Не бе намерено превозно средство между двете дестинации:");
                    workflow.changeState(new FailedState());
                    transferModel.setResponseInfo("Не беше намерено превозно средство");
                }
            }
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Something terrible happened in state CreateRoutePathState: " + ex.getMessage());
            workflow.changeState(new FailedState());
        }
    }
}
