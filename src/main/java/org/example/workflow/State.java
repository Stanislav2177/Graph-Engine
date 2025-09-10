package org.example.workflow;

public interface State {
    void onHandle(Workflow workflow, StatesTransferModel transferModel) throws Exception;
}
