package org.example;

import java.util.*;

public class RideService {
    private final Map<String, Driver> drivers = new HashMap<>();
    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Ride> rides = new HashMap<>();

    public void addDriver(String id, double latitude, double longitude) {
        drivers.put(id, new Driver(id, latitude, longitude));
    }

    public void addRider(String id, double latitude, double longitude) {
        riders.put(id, new Rider(latitude, longitude));
    }

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

        availableDrivers.sort((d1, d2) -> {
            boolean isD1Preferred = rider.getPreferredDrivers().contains(d1.getId());
            boolean isD2Preferred = rider.getPreferredDrivers().contains(d2.getId());
            if (isD1Preferred != isD2Preferred) {
                return isD1Preferred ? -1 : 1;
            }
            return Double.compare(d2.getRating(), d1.getRating());
        });

        return availableDrivers;
    }

    public Ride startRide(String rideId, String riderId, String driverId) {
        Rider rider = riders.get(riderId);
        Driver driver = drivers.get(driverId);
        if (rider == null || driver == null || !driver.isAvailable()) {
            System.out.println("INVALID_RIDE_REQUEST");
            return null;
        }
        Ride ride = new Ride(rider, driver);
        rides.put(rideId, ride);
        driver.setAvailable(false);
        System.out.println("RIDE_STARTED: " + rideId);
        return ride;
    }

    public void stopRide(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.isCompleted()) {
            System.out.println("INVALID_RIDE");
            return;
        }
        ride.completeRide();
        System.out.println("RIDE_STOPPED: " + rideId);
    }

    public void rateDriver(String rideId, int rating) {
        Ride ride = rides.get(rideId);
        if (ride == null || !ride.isCompleted()) {
            System.out.println("INVALID_RIDE");
            return;
        }
        ride.getDriver().addRating(rating);
        System.out.println("DRIVER_RATED");
    }

    public void addPreferredDriver(String riderId, String driverId) {
        Rider rider = riders.get(riderId);
        Driver driver = drivers.get(driverId);
        if (rider == null || driver == null) {
            System.out.println("INVALID_RIDER_OR_DRIVER");
            return;
        }
        rider.addPreferredDriver(driverId);
        System.out.println("PREFERRED_DRIVER_ADDED");
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Add this method to resolve the error
    public Map<String, Ride> getRides() {
        return rides;
    }
}
