package org.example.workflow;

import org.example.logger.Logger;
import org.example.models.TransportDataStatuses;
import org.example.transport.Trip;
import org.example.transport.TripStatus;
import org.example.transport.TripTrackingService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class TravelingState implements State {
    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) {
        try {
            Date correctedStartHour = new Date(transferModel.getTripDataDTO().getStartHour().getTime() - (3 * 60 * 60 * 1000)); // Subtract 3 hours
            Date correctedEndHour = new Date(transferModel.getTripDataDTO().getEndHour().getTime() - (3 * 60 * 60 * 1000)); // Subtract 3 hours



            LocalDateTime  localTimeStart = correctedStartHour.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime  localTimeEnd = correctedEndHour.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Trip trip = new Trip(transferModel.getVehicle().getVehicleId(),
                    transferModel.getDB_routePathId(),
                    localTimeStart,
                    localTimeEnd,
                    localTimeEnd.plusMinutes((long) ((long) transferModel.getTotalWeight()*1.1)),
                    TransportDataStatuses.scheduled);
            List<Trip> allTrips = workflow.getTransportService().getAllTrips();
            int tripId = workflow.getTransportService().createTrip(trip);

            if (tripId > 0) {
                trip.setTripId(tripId);
                trip.setStatus(TripStatus.TRAVELING);
                trip.setActualArrival(LocalDateTime.now());

                for (Trip t: allTrips) {
                    if(t.getVehicleId() == trip.getVehicleId() && t.getRoutePathId() == trip.getRoutePathId()){
                        transferModel.setSuccessful(false);
                        transferModel.setResponseInfo("Вече съществува подобно пътуване");
                        workflow.changeState(new FailedState());
                        return;
                    }
                }

                TripTrackingService.getInstance().startTrackingTrip(trip);
                Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Trip is started: ");
                transferModel.setSuccessful(true);
                workflow.setWorking(false);
            }else{
                workflow.changeState(new FailedState());
                transferModel.setResponseInfo("Problem with saving the trip");
            }
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Something terrible happened in state TravelingState: " + ex.getMessage());
            workflow.setState(new FailedState());
        }
    }
}
