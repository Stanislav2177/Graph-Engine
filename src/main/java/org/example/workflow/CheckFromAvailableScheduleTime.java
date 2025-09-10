package org.example.workflow;

import org.example.logger.Logger;
import org.example.models.ApiResponse;
import org.example.models.ScheduleTransportDTO;
import org.example.models.TransportVehicle;
import org.example.models.TripDataDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckFromAvailableScheduleTime implements State {

    @Override
    public void onHandle(Workflow workflow, StatesTransferModel transferModel) throws Exception {
        try {
            if (transferModel.getDB_routePathId() != -1) {
                List<ScheduleTransportDTO> allSchedulesList = workflow.getTransportService().getAllSchedulesList();

                List<String> availableSchedulesForThatNodes = new ArrayList<>();
                boolean foundSchedule = false;
                boolean missingDay = false;

                for (ScheduleTransportDTO schedule : allSchedulesList) {
                    if (schedule.getTargetNodeId() == transferModel.getTarget().getId()
                            && schedule.getSourceNodeId() == transferModel.getSource().getId()) {

                        foundSchedule = true;

                        Date dailyEndHour = schedule.getDailyEndHour();
                        Date dailyStartHour = schedule.getDailyStartHour();

                        TripDataDTO tripDataDTO = transferModel.getTripDataDTO();
                        Date startHour = tripDataDTO.getStartHour();
                        Date endHour = tripDataDTO.getEndHour();
                        Date correctedStartHour = new Date(startHour.getTime() - (3 * 60 * 60 * 1000)); // Subtract 3 hours
                        Date correctedEndHour = new Date(endHour.getTime() - (3 * 60 * 60 * 1000)); // Subtract 3 hours

                        if (correctedStartHour != null && correctedEndHour != null && dailyStartHour != null && dailyEndHour != null) {
                            boolean isStartValid = isTimeBetween(correctedStartHour, dailyStartHour, dailyEndHour, false);
                            boolean isEndValid = isTimeBetween(correctedEndHour, dailyStartHour, dailyEndHour, false);

                            if (isStartValid && isEndValid) {
                                // Proceed with your logic
                                for (String availableDay : schedule.getAvailableDays()) {
                                    List<String> availableDays = tripDataDTO.getAvailableDays();
                                   availableDay = availableDay.replace("[", "");
                                   availableDay = availableDay.replace("]", "");
                                    availableDay = availableDay.replace("\"", "");
                                    availableDay =availableDay.replace("\"", "");
                                    availableDay = availableDay.replace(" ", "");
                                    if (availableDays.size() > 0 && availableDays.contains(availableDay)) {
                                        transferModel.setResponseInfo("Всичките критерии са изпълнение");
                                        workflow.changeState(new TravelingState());
                                        return;
                                    }
                                    missingDay = true;
                                }
                            }
                        }
                        if(missingDay){
                            transferModel.setResponseInfo("Не бе намерен ден от седмицата, който да отговаря на избраният от оператора.");
                            transferModel.setSuccessful(false);
                            workflow.changeState(new FailedState());
                            return;
                        }

                        availableSchedulesForThatNodes.add("Start: " + schedule.getDailyStartHour()  + "; End: "+ schedule.getDailyEndHour());
                    }
                }

                if(!foundSchedule){
                    transferModel.setResponseInfo("Не бе намерен подходяш график, опитайте с други дни от седмицата" );
                }

                transferModel.setResponseInfo("Не е намерен подходящ график, опитайте с други времеви параметри");
                for (String availableSchedulesForThatNode : availableSchedulesForThatNodes) {
                    transferModel.setResponseInfo("Позволен Диапазон: " + availableSchedulesForThatNode);
                }
                transferModel.setSuccessful(false);
                workflow.changeState(new FailedState());
            }
        } catch (Exception ex) {
            Logger.getInstance().writeLog(Logger.Log.ERROR, Logger.Queue.Flow, "Something terrible happened in state CreateRoutePathState: " + ex.getMessage());
            workflow.changeState(new FailedState());
            throw  new Exception();
        }
    }
    private boolean isTimeBetween(Date timeToCheck, Date startTime, Date endTime, boolean opositeLogic ) {

        if (timeToCheck == null || startTime == null || endTime == null) {
            return false;
        }

        // Extract hours and minutes
        Calendar cal = Calendar.getInstance();

        cal.setTime(timeToCheck);
        int checkHour = cal.get(Calendar.HOUR_OF_DAY);
        int checkMinute = cal.get(Calendar.MINUTE);

        cal.setTime(startTime);
        int startHour = cal.get(Calendar.HOUR_OF_DAY);
        int startMinute = cal.get(Calendar.MINUTE);

        cal.setTime(endTime);
        int endHour = cal.get(Calendar.HOUR_OF_DAY);
        int endMinute = cal.get(Calendar.MINUTE);

        // Compare hours and minutes
        if (checkHour > startHour || (checkHour == startHour && checkMinute >= startMinute)) {
            if (checkHour < endHour || (checkHour == endHour && checkMinute <= endMinute)) {
                if(opositeLogic){
                    return false;
                }

                return true;
            }
        }
        if(opositeLogic){
            return true;
        }
        return false;
    }
}
