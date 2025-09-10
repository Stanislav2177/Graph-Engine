package org.example.transport;

import org.apache.commons.configuration.ConfigurationException;
import org.example.logger.Logger;
import org.example.repository.TransportDataRepo;

public class TripTracker implements Runnable {
    private final Trip trip;
    private volatile boolean running = true;
    private static final long CHECK_INTERVAL = 60000;
    private TransportDataRepo transportDataRepo;


    public TripTracker(Trip trip) throws ConfigurationException {
        this.trip = trip;
        transportDataRepo = new TransportDataRepo();
    }

    @Override
    public void run() {
        try {
            while (running && !Thread.currentThread().isInterrupted()) {
                Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Tracking: " + trip.getTripId());
                double progress = calculateTripProgress(trip);

                updateTripStatus(trip, progress);

                if (progress >= 1.0) {
                    completeTrip(trip);
                    Logger.getInstance().writeLog(Logger.Log.SUCCESS, Logger.Queue.Flow, "Trip Finished ");
                    break;
                }

                Thread.sleep(CHECK_INTERVAL);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            cleanupResources();
        }
    }

    public void stop() {
        this.running = false;
    }

    private double calculateTripProgress(Trip trip) {
        long elapsed = System.currentTimeMillis() - trip.getActualDeparture().getMinute();
        return Math.min(1.0, (double) elapsed / trip.getActualArrival().getMinute());
    }

    private void updateTripStatus(Trip trip, double progress) {
        trip.setProgress(progress);
        transportDataRepo.updateTransportData(trip);
    }

    private void completeTrip(Trip trip) {
        return;
    }

    private void cleanupResources() {
        return;
    }
}