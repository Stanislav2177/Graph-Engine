package org.example.workflow;

import org.apache.commons.configuration.ConfigurationException;
import org.example.graph.Node;
import org.example.models.TripDataDTO;

public class WorkflowMechanism extends Workflow {
    private Workflow workflow;
    private StatesTransferModel transferModel;

    public WorkflowMechanism() throws ConfigurationException {
        workflow = new Workflow();
    }

    public StatesTransferModel getTransferModel() {
        return transferModel;
    }

    public void startFlow(Node source, Node target, TripDataDTO tripDataDTO) throws Exception {
        transferModel = new StatesTransferModel();
        transferModel.setSource(source);
        transferModel.setTarget(target);
        transferModel.setTripDataDTO(tripDataDTO);

        while(true){
            if(workflow.isWorking()) {
                workflow.getState().onHandle(workflow, transferModel);
            }else{
                break;
            }
        }
    }
}
