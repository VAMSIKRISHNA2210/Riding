package org.example;

import java.util.*;

public class RideService {
    private final Map<String, Driver> drivers = new HashMap<>();
    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Ride> rides = new HashMap<>();

    // Add a driver to the system
    public void addDriver(String id, String name, double latitude, double longitude) {
        drivers.put(id, new Driver(id, name, latitude, longitude));
    }

    // Add a rider to the system
    public void addRider(String id, String name, double latitude, double longitude) {
        riders.put(id, new Rider(id, name, latitude, longitude));
    }

    // Match a rider with nearby drivers (within 5 km)
    public List<Driver> matchDrivers(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            System.out.println("RIDER_NOT_FOUND");
            return Collections.emptyList();
        }

        List<Driver> availableDrivers = new ArrayList<>();
        for (Driver driver : drivers.values()) {
            if (driver.isAvailable() && distance(rider.getLatitude(), rider.getLongitude(), driver.getLatitude(), driver.getLongitude()) <= 5) {
                availableDrivers.add(driver);
            }
        }

        availableDrivers.sort(Comparator.comparingDouble(driver ->
                distance(rider.getLatitude(), rider.getLongitude(), driver.getLatitude(), driver.getLongitude())));

        return availableDrivers;
    }

    // Start a ride with a specific driver
    public Ride startRide(String rideId, String riderId, String driverId) {
        Rider rider = riders.get(riderId);
        Driver driver = drivers.get(driverId);

        if (rider == null || driver == null || !driver.isAvailable()) {
            System.out.println("INVALID_RIDE_REQUEST");
            return null;
        }

        Ride ride = new Ride(rideId, rider, driver);
        rides.put(rideId, ride);
        driver.setAvailable(false); // Mark the driver as unavailable
        System.out.println("RIDE_STARTED: " + rideId);
        return ride;
    }

    // Stop a ride
    public void stopRide(String rideId, double endLatitude, double endLongitude, int duration) {
        Ride ride = rides.get(rideId);

        if (ride == null || ride.isCompleted()) {
            System.out.println("INVALID_RIDE");
            return;
        }

        ride.completeRide(endLatitude, endLongitude, duration);
        System.out.println("RIDE_STOPPED: " + rideId);
    }

    // Get all rides
    public Map<String, Ride> getRides() {
        return rides;
    }

    // Calculate distance between two points
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
