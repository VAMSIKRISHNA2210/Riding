package org.example.service;

import lombok.Getter;
import org.example.model.Driver;
import org.example.model.Rider;
import org.example.model.Ride;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class RideService {

    // Get all drivers (for testing purposes)
    // Thread-safe maps for storing drivers, riders, and rides
    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    // Get all riders (for testing purposes)
    private final Map<String, Rider> riders = new ConcurrentHashMap<>();
    private final Map<String, Ride> rides = new ConcurrentHashMap<>();

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
        if (rider == null) throw new IllegalArgumentException("Rider not found");

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

    public void addPreferredDriver(String riderId, String driverId) {
        Rider rider = riders.get(riderId);
        Driver driver = drivers.get(driverId);

        if (rider == null) {
            throw new IllegalArgumentException("Rider not found: " + riderId);
        }
        if (driver == null) {
            throw new IllegalArgumentException("Driver not found: " + driverId);
        }

        rider.addPreferredDriver(driverId);
        System.out.println("Preferred driver added successfully for rider: " + riderId);
    }

    // Start a ride with a specific driver
    public Ride startRide(String rideId, String riderId, String driverId) {
        Rider rider = riders.get(riderId);
        Driver driver = drivers.get(driverId);

        if (rider == null || driver == null || !driver.isAvailable()) throw new IllegalArgumentException("Invalid ride request");

        Ride ride = new Ride(rideId, rider, driver);
        rides.put(rideId, ride);
        driver.setAvailable(false); // Mark the driver as unavailable during the ride

        return ride;
    }

    // Stop a ride and mark it as completed
    public void stopRide(String rideId, double endLatitude, double endLongitude, int duration) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.isCompleted()) throw new IllegalArgumentException("Invalid ride");

        ride.completeRide(endLatitude, endLongitude, duration);
    }

    // Rate a driver for a completed ride
    public void rateDriver(String rideId, int rating) {
        Ride ride = rides.get(rideId);
        if (ride == null || !ride.isCompleted()) throw new IllegalArgumentException("Invalid ride");

        ride.getDriver().addRating(rating);
    }

    // Calculate distance between two points using Euclidean distance formula
    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
