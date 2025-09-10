package org.example.transport;

import org.apache.commons.configuration.ConfigurationException;

import java.util.concurrent.*;

public class TripTrackingService {
    private static TripTrackingService instance;
    private final ExecutorService executor;
    private final ConcurrentMap<Integer, TripTracker> activeTrackers;
    private final ConcurrentMap<Integer, Future<?>> futures;

    private TripTrackingService() {
        this.executor = Executors.newCachedThreadPool();
        this.activeTrackers = new ConcurrentHashMap<>();
        this.futures = new ConcurrentHashMap<>();
    }

    public static synchronized TripTrackingService getInstance() {
        if (instance == null) {
            instance = new TripTrackingService();
        }
        return instance;
    }

    public synchronized void startTrackingTrip(Trip trip) throws ConfigurationException {
        int tripId = trip.getTripId();
        if (activeTrackers.containsKey(tripId)) {
            System.out.println("Trip " + tripId + " is already being tracked.");
            return;
        }

        TripTracker tracker = new TripTracker(trip);
        Future<?> future = executor.submit(tracker);

        activeTrackers.put(tripId, tracker);
        futures.put(tripId, future);
    }

    public synchronized void stopTrackingTrip(int tripId) {
        TripTracker tracker = activeTrackers.remove(tripId);
        Future<?> future = futures.remove(tripId);

        if (tracker != null) {
            tracker.stop();
        }
        if (future != null) {
            future.cancel(true);
        }

        System.out.println("Stopped tracking trip " + tripId);
    }

    public synchronized void shutdown() {
        for (Integer tripId : activeTrackers.keySet()) {
            stopTrackingTrip(tripId);
        }
        executor.shutdownNow();
        System.out.println("All trip tracking shut down.");
    }

    public boolean isTrackingTrip(int tripId) {
        return activeTrackers.containsKey(tripId);
    }

    public int getActiveTripCount() {
        return activeTrackers.size();
    }
}
