package org.example.service;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
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
     * Generates a bill for a completed ride, formatted for API output.
     * This method retrieves the ride details, calculates the total fare,
     * and formats it with a precision of 2 decimal places, suitable for API responses.
     *
     * @param rideId The ID of the ride for which to generate the bill.
     * @return A string containing the formatted bill details, including the ride ID,
     *         driver ID, and total fare, or "INVALID_RIDE" if the ride is not found
     *         or has not been completed.
     */
    public String generateBillForApi(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || !ride.isCompleted()) {
            return "INVALID_RIDE";
        }

        BigDecimal totalFare = calculateFare(ride);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(2); // API precision: 2 decimals
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        String formattedTotalFare = numberFormat.format(totalFare);

        return String.format("BILL %s %s %s", rideId, ride.getDriver().getId(), formattedTotalFare);
    }

    /**
     * Generates a bill for a completed ride, formatted for CLI output.
     * This method retrieves the ride details, calculates the total fare,
     * and formats it with a precision of 4 decimal places, suitable for CLI display.
     *
     * @param rideId The ID of the ride for which to generate the bill.
     * @return A string containing the formatted bill details, including the ride ID
     *         and total fare, or "INVALID_RIDE" if the ride is not found
     *         or has not been completed.
     */
    public String generateBillForCli(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || !ride.isCompleted()) {
            return "INVALID_RIDE";
        }

        BigDecimal totalFare = calculateFare(ride);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(4); // CLI precision: 4 decimals
        numberFormat.setMaximumFractionDigits(4);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        String formattedTotalFare = numberFormat.format(totalFare);

        return String.format("Total Bill: %s %s", rideId, formattedTotalFare);
    }

    /**
     * Calculates the total fare for a ride based on distance and duration.
     * This method retrieves the ride details and calculates the total fare
     * using the specified rates for distance and duration.
     *
     * @param ride The Ride object for which to calculate the fare.
     * @return A BigDecimal representing the total fare for the ride.
     */
    private BigDecimal calculateFare(Ride ride) {
        BigDecimal distance = calculateDistanceBigDecimal(
                BigDecimal.valueOf(ride.getStartLatitude()),
                BigDecimal.valueOf(ride.getStartLongitude()),
                BigDecimal.valueOf(ride.getEndLatitude()),
                BigDecimal.valueOf(ride.getEndLongitude())
        );
        BigDecimal duration = BigDecimal.valueOf(ride.getDuration());

        BigDecimal baseFare = new BigDecimal("50");
        BigDecimal distanceFare = new BigDecimal("6.5").multiply(distance);
        BigDecimal timeFare = new BigDecimal("2").multiply(duration);

        return baseFare.add(distanceFare).add(timeFare).multiply(new BigDecimal("1.2"));
    }


    /**
     * Calculates the distance between two points.
     *
     * @param x1   The latitude of the first point.
     * @param y1   The longitude of the first point.
     * @param x2   The latitude of the second point.
     * @param y2   The longitude of the second point.
     * @return The distance between the two points as a BigDecimal.
     */
    private BigDecimal calculateDistanceBigDecimal(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        BigDecimal dx = x2.subtract(x1);
        BigDecimal dy = y2.subtract(y1);
        BigDecimal distanceSquared = dx.multiply(dx).add(dy.multiply(dy));
        BigDecimal distance = distanceSquared.sqrt(new MathContext(10, RoundingMode.HALF_UP));
        return distance.multiply(new BigDecimal("0.1"));
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
