package org.example.service;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service class for managing ride operations.
 * This class handles driver and rider management, ride matching, and billing.
 */
@Service
public class RideService {
    private static final BigDecimal SERVICE_TAX_MULTIPLIER = new BigDecimal("1.2");
    private static final BigDecimal BASE_FARE = new BigDecimal("50");
    private static final BigDecimal DISTANCE_FARE_RATE = new BigDecimal("6.5");
    private static final BigDecimal TIME_FARE_RATE = new BigDecimal("2");
    private static final BigDecimal MAX_DISTANCE_RADIUS = new BigDecimal("5.0");
    private static final MathContext DISTANCE_CALCULATION_CONTEXT = new MathContext(10);

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
                .filter(driver -> calculateDistance(
                        BigDecimal.valueOf(rider.getLatitude()),
                        BigDecimal.valueOf(rider.getLongitude()),
                        BigDecimal.valueOf(driver.getLatitude()),
                        BigDecimal.valueOf(driver.getLongitude())
                ).compareTo(MAX_DISTANCE_RADIUS) <= 0) // 5 km radius
                .sorted(Comparator.comparing(driver -> calculateDistance(
                        BigDecimal.valueOf(rider.getLatitude()),
                        BigDecimal.valueOf(rider.getLongitude()),
                        BigDecimal.valueOf(driver.getLatitude()),
                        BigDecimal.valueOf(driver.getLongitude())
                )))
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
     * @return The ride ID if successfully started
     * @throws IllegalArgumentException if the ride cannot be started due to invalid input
     */
    public String startRide(String rideId, int n, String riderId) {
        List<String> matchedDrivers = matchRider(riderId);
        if (n > matchedDrivers.size() || rides.containsKey(rideId)) {
            throw new IllegalArgumentException("Invalid ride or already exists");
        }

        String driverId = matchedDrivers.get(n - 1);
        Driver driver = drivers.get(driverId);
        Rider rider = riders.get(riderId);

        driver.setAvailable(false);
        Ride ride = new Ride(rideId, driver, rider);
        rides.put(rideId, ride);

        return rideId;
    }

    /**
     * Stops an ongoing ride.
     *
     * @param rideId       The ID of the ride to stop
     * @param endLatitude  The end latitude of the ride
     * @param endLongitude The end longitude of the ride
     * @param duration     The duration of the ride in minutes
     * @return The ride ID if successfully stopped
     * @throws IllegalArgumentException if the ride is invalid or already completed
     */
    public String stopRide(String rideId, double endLatitude, double endLongitude, double duration) {
        Ride ride = rides.get(rideId);
        if (ride == null || ride.isCompleted()) {
            throw new IllegalArgumentException("Invalid or already completed ride");
        }

        ride.endRide(endLatitude, endLongitude, duration);
        ride.getDriver().setAvailable(true);

        return rideId;
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param rideId The ID of the ride for which to generate the bill.
     * @return An Optional containing BillDetails if valid; empty Optional otherwise.
     */
    public Optional<BillDetails> generateBill(String rideId) {
        Ride ride = rides.get(rideId);
        if (ride == null || !ride.isCompleted()) {
            return Optional.empty(); // Invalid or incomplete ride
        }

        BigDecimal totalFare = calculateFare(ride).setScale(2, RoundingMode.HALF_EVEN);

        return Optional.of(new BillDetails(rideId, ride.getDriver().getId(), totalFare));
    }

    /**
     * Calculates the total fare for a completed ride.
     *
     * @param ride The Ride object for which to calculate the fare.
     * @return A BigDecimal representing the total fare for the ride.
     */
    private BigDecimal calculateFare(Ride ride) {
        BigDecimal distance = calculateDistance(
                BigDecimal.valueOf(ride.getStartLatitude()),
                BigDecimal.valueOf(ride.getStartLongitude()),
                BigDecimal.valueOf(ride.getEndLatitude()),
                BigDecimal.valueOf(ride.getEndLongitude())
        );
        BigDecimal duration = BigDecimal.valueOf(ride.getDuration());

        BigDecimal distanceFare = DISTANCE_FARE_RATE.multiply(distance);
        BigDecimal timeFare = TIME_FARE_RATE.multiply(duration);

        return BASE_FARE.add(distanceFare).add(timeFare).multiply(SERVICE_TAX_MULTIPLIER);
    }

    /**
     * Calculates the distance between two points using BigDecimal arithmetic.
     *
     * @param startLatitude  Latitude of the first point
     * @param startLongitude Longitude of the first point
     * @param endLatitude    Latitude of the second point
     * @param endLongitude   Longitude of the second point
     * @return The calculated distance in kilometers as a BigDecimal.
     */
    private BigDecimal calculateDistance(BigDecimal startLatitude, BigDecimal startLongitude,
                                         BigDecimal endLatitude, BigDecimal endLongitude) {
        BigDecimal dx = endLatitude.subtract(startLatitude);
        BigDecimal dy = endLongitude.subtract(startLongitude);
        return dx.multiply(dx).add(dy.multiply(dy)).sqrt(DISTANCE_CALCULATION_CONTEXT).multiply(new BigDecimal("0.1"));
    }
}
