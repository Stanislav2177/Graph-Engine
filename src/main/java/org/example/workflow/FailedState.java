package org.example.workflow;

import org.example.logger.Logger;

public class FailedState implements State{
    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) throws Exception {
        try{
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Flow reached Failed State");
            workflow.setWorking(false);
            transferModel.setResponseInfo("Процеса за регистриране на ново пътуване бе спрян!");

        }catch (Exception ex){
            workflow.setWorking(false);
            transferModel.setResponseInfo("Exception: " + ex.getMessage());
        }
    }
}
