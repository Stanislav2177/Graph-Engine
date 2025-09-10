package org.example.workflow;

import org.apache.commons.configuration.ConfigurationException;
import org.example.transport.TransportService;

public class Workflow {
    private State state;
    private TransportService transportService;
    private boolean isWorking;
    public Workflow() throws ConfigurationException {
        state = new StartState();
        transportService= new TransportService();
        isWorking = true;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public TransportService getTransportService() {
        return transportService;
    }

    public void setTransportService(TransportService transportService) {
        this.transportService = transportService;
    }

    public void changeState(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }
}
