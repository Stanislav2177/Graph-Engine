package org.example.workflow;

import org.example.logger.Logger;
import org.example.repository.NodesRepo;

public class StartState implements State {

    /// Starting point of the process for traveling
    /// No special functionality, only information is received from this state.
    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) {
        try{
            Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Start State is triggered for source: " + transferModel.getSource().getLabel() + " | target: " + transferModel.getTarget().getLabel());
            workflow.changeState(new CheckState());
        }catch (Exception ex){
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Something terrible happened in state START: " + ex.getMessage());
            workflow.changeState(new FailedState());
        }
    }
}
