package org.example.service;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service class for managing ride-sharing operations.
 * This class handles driver and rider management, ride matching, and billing.
 */
@Service
public class RideService {
    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    private final Map<String, Rider> riders = new ConcurrentHashMap<>();
    private final Map<String, Ride> rides = new ConcurrentHashMap<>();

    /**
     * Adds a new driver to the system.
     *
     * @param id        The unique identifier for the driver
     * @param latitude  The initial latitude of the driver's location
     * @param longitude The initial longitude of the driver's location
     */
    public void addDriver(String id, double latitude, double longitude) {
        drivers.put(id, new Driver(id, latitude, longitude));
    }

    /**
     * Adds a new rider to the system.
     *
     * @param id        The unique identifier for the rider
     * @param latitude  The initial latitude of the rider's location
     * @param longitude The initial longitude of the rider's location
     */
    public void addRider(String id, double latitude, double longitude) {
        riders.put(id, new Rider(id, latitude, longitude));
    }

    /**
     * Matches a rider with nearby available drivers.
     *
     * @param riderId The ID of the rider requesting a match
     * @return A list of driver IDs sorted by proximity, limited to 5 matches
     */
    public List<String> matchRider(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) return Collections.emptyList();

        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .filter(driver -> {
                    try {
                        double distance = calculateDistance(
                                rider.getLatitude(), rider.getLongitude(),
                                driver.getLatitude(), driver.getLongitude()
                        );
                        return distance <= 5.0; // 5 km radius
                    } catch (IllegalArgumentException e) {
                        return false; // Invalid coordinates, don't match this driver
                    }
                })
                .sorted(Comparator.comparingDouble(driver -> {
                    try {
                        return calculateDistance(
                                rider.getLatitude(), rider.getLongitude(),
                                driver.getLatitude(), driver.getLongitude()
                        );
                    } catch (IllegalArgumentException e) {
                        return Double.MAX_VALUE; // Invalid coordinates, sort to the end
                    }
                }))
                .limit(5)
                .map(Driver::getId)
                .collect(Collectors.toList());
    }

    /**
     * Starts a new ride.
     *
     * @param rideId  The unique identifier for the ride
     * @param n       The index of the chosen driver from the matched list
     * @param riderId The ID of the rider starting the ride
     * @return A string indicating the status of the ride start
     */
    public String startRide(String rideId, int n, String riderId) {
        List<String> matchedDrivers = matchRider(riderId);
        if (n > matchedDrivers.size() || rides.containsKey(rideId)) {
            return "INVALID_RIDE";
        }

        String driverId = matchedDrivers.get(n - 1);
        Driver driver = drivers.get(driverId);
        Rider rider = riders.get(riderId);

        driver.setAvailable(false);
        Ride ride = new Ride(rideId, driver, rider);
        rides.put(rideId, ride);

        return "RIDE_STARTED " + rideId;
    }

    /**
     * Stops an ongoing ride.
     *
     * @param rideId   The ID of the ride to stop
     * @param endX     The end latitude of the ride
     * @param endY     The end longitude of the ride
     * @param duration The duration of the ride in minutes
     * @return A string indicating the status of the ride stop
     */
    public String stopRide(String rideId, double endX, double endY, double duration) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.isCompleted()) {
            return "INVALID_RIDE";
        }

        ride.endRide(endX, endY, duration);
        ride.getDriver().setAvailable(true);

        return "RIDE_STOPPED " + rideId;
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param rideId The ID of the ride to generate the bill for
     * @return A string containing the bill details or an error message
     */
    public String generateBill(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || !ride.isCompleted()) {
            return "INVALID_RIDE";
        }

        double distance = calculateDistance(
                ride.getStartLatitude(),
                ride.getStartLongitude(),
                ride.getEndLatitude(),
                ride.getEndLongitude()
        );

        double duration = ride.getDuration();

        double fare = 50 + (6.5 * distance) + (2 * duration); // Base fare + distance fare + time fare
        double totalFare = fare * 1.2; // Adding 20% service tax

        return String.format("BILL %s %s %.2f", rideId, ride.getDriver().getId(), totalFare);
    }

    /**
     * Calculates the distance between two points using the Haversine formula.
     *
     * @param x1 Latitude of the first point
     * @param y1 Longitude of the first point
     * @param x2 Latitude of the second point
     * @param y2 Longitude of the second point
     * @return The calculated distance in kilometers
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy) * 0.1; // 0.1 km per unit
    }
}
