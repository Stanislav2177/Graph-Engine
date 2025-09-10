package org.example.workflow;

import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.repository.TransportRoutesRepo;

public class CreateRoutePathState implements State{
    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) {
        try{
            int transportRoute = workflow.getTransportService().createTransportRoute(transferModel.getRouteIds(), transferModel.getSource().getId(), transferModel.getTarget().getId());

            if(transportRoute != -1){
                transferModel.setDB_routePathId(transportRoute);
                workflow.changeState(new CheckForVehicleState());
                Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Route is create with id: " + transportRoute);
            }else{
                transferModel.setResponseInfo("Cant be created route between: " + transferModel.getSource().getLabel() + " and " + transferModel.getTarget().getLabel());
                workflow.changeState(new FailedState());
                transferModel.setResponseInfo("Проблем при създаването на маршрут");
            }

        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Something terrible happened in state CreateRoutePathState: " + ex.getMessage());
            workflow.changeState(new FailedState());
        }
    }
}
