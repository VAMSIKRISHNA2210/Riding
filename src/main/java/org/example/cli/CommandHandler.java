package org.example.cli;

import org.example.service.BillDetails;
import org.example.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles commands for the CLI application.
 */
@Component
public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final RideService rideService;

    public CommandHandler(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Validates latitude values.
     *
     * @param latitude The latitude value to validate.
     * @return True if valid; false otherwise.
     */
    private boolean isValidLatitude(double latitude) {
        return !(latitude >= -90) || !(latitude <= 90);
    }

    /**
     * Validates longitude values.
     *
     * @param longitude The longitude value to validate.
     * @return True if valid; false otherwise.
     */
    private boolean isValidLongitude(double longitude) {
        return !(longitude >= -180) || !(longitude <= 180);
    }

    /**
     * Adds a new driver to the system.
     *
     * @param parts Command parts containing driver details.
     */
    public void addDriver(String[] parts) {
        if (parts.length != 4) {
            logger.error("Invalid parameters for ADD_DRIVER. Usage: ADD_DRIVER <driverId> <latitude> <longitude>");
            System.out.println("Invalid parameters entered. Usage: ADD_DRIVER <driverId> <latitude> <longitude>");
            return;
        }

        try {
            String id = parts[1];
            double latitude = Double.parseDouble(parts[2]);
            double longitude = Double.parseDouble(parts[3]);

            if (isValidLatitude(latitude) || isValidLongitude(longitude)) {
                logger.error("Invalid latitude or longitude values for driver: {}", id);
                System.out.println("Error: Invalid latitude or longitude values.");
                return;
            }

            rideService.addDriver(id, latitude, longitude);
            logger.info("Driver added successfully. Driver ID: {}", id);
            System.out.println("Driver added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Latitude and longitude must be valid numbers.");
        }
    }

    /**
     * Adds a new rider to the system.
     *
     * @param parts Command parts containing rider details.
     */
    public void addRider(String[] parts) {
        if (parts.length != 4) {
            logger.error("Invalid parameters for ADD_RIDER. Usage: ADD_RIDER <riderId> <latitude> <longitude>");
            System.out.println("Invalid parameters entered. Usage: ADD_RIDER <riderId> <latitude> <longitude>");
            return;
        }

        try {
            String id = parts[1];
            double latitude = Double.parseDouble(parts[2]);
            double longitude = Double.parseDouble(parts[3]);

            if (isValidLatitude(latitude) || isValidLongitude(longitude)) {
                logger.error("Invalid latitude or longitude values for rider: {}", id);
                System.out.println("Error: Invalid latitude or longitude values.");
                return;
            }

            rideService.addRider(id, latitude, longitude);
            logger.info("Rider added successfully. Rider ID: {}", id);
            System.out.println("Rider added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Latitude and longitude must be valid numbers.");
        }
    }

    /**
     * Matches a rider with available drivers.
     *
     * @param parts Command parts containing rider ID.
     */
    public void matchRider(String[] parts) {
        if (parts.length != 2) {
            logger.error("Invalid parameters for MATCH. Usage: MATCH <riderId>");
            System.out.println("Invalid parameters entered. Usage: MATCH <riderId>");
            return;
        }

        String riderId = parts[1];
        List<String> matches = rideService.matchRider(riderId);

        if (matches.isEmpty()) {
            logger.warn("No drivers matched for Rider ID: {}", riderId);
            System.out.println("No drivers matched for Rider ID: " + riderId);
        } else {
            logger.info("Matched drivers for Rider ID {}: {}", riderId, String.join(", ", matches));
            System.out.println("Matched drivers: " + String.join(", ", matches));
        }
    }

    /**
     * Starts a new ride.
     *
     * @param parts Command parts containing ride details.
     */
    public void startRide(String[] parts) {
        if (parts.length != 4) {
            logger.error("Invalid parameters for START_RIDE. Usage: START_RIDE <rideId> <driverIndex> <riderId>");
            System.out.println("Invalid parameters entered. Usage: START_RIDE <rideId> <driverIndex> <riderId>");
            return;
        }

        try {
            String rideId = parts[1];
            int driverIndex = Integer.parseInt(parts[2]);
            String riderId = parts[3];

            if (driverIndex <= 0) {
                logger.error("Driver index must be greater than 0.");
                System.out.println("Error: Driver index must be greater than 0.");
                return;
            }

            String result = rideService.startRide(rideId, driverIndex, riderId);
            logger.info("Ride started successfully. Ride ID: {}", result);
            System.out.println("Ride started successfully.");
        } catch (NumberFormatException e) {
            logger.error("Driver index must be a valid integer.", e);
            System.out.println("Error: Driver index must be a valid integer.");
        } catch (IllegalArgumentException e) {
            logger.error("Error starting ride: {}", e.getMessage());
            System.out.println("Error starting ride: " + e.getMessage());
        }
    }

    /**
     * Stops an ongoing ride.
     *
     * @param parts Command parts containing ride stop details.
     */
    public void stopRide(String[] parts) {
        if (parts.length != 5) {
            logger.error("Invalid parameters for STOP_RIDE. Usage: STOP_RIDE <rideId> <endLatitude> <endLongitude> <duration>");
            System.out.println("Invalid parameters entered. Usage: STOP_RIDE <rideId> <endLatitude> <endLongitude> <duration>");
            return;
        }

        try {
            String rideId = parts[1];
            double endLatitude = Double.parseDouble(parts[2]);
            double endLongitude = Double.parseDouble(parts[3]);
            double duration = Double.parseDouble(parts[4]);

            if (isValidLatitude(endLatitude) || isValidLongitude(endLongitude)) {
                logger.error("Invalid latitude or longitude values.");
                System.out.println("Error: Invalid latitude or longitude values.");
                return;
            }

            if (duration <= 0) {
                logger.error("Duration must be greater than 0.");
                System.out.println("Error: Duration must be greater than 0.");
                return;
            }

            String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            logger.info("Ride stopped successfully. Ride ID: {}", result);
            System.out.println("Ride stopped successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Latitude, longitude, and duration must be valid numbers.");
        } catch (IllegalArgumentException e) {
            logger.error("Error stopping ride: {}", e.getMessage());
            System.out.println("Error stopping ride: " + e.getMessage());
        }
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param parts Command parts containing ride ID.
     */
    public void generateBill(String[] parts) {
        if (parts.length != 2) {
            logger.error("Invalid parameters for BILL. Usage: BILL <rideId>");
            System.out.println("Invalid parameters entered. Usage: BILL <rideId>");
            return;
        }

        String rideId = parts[1];
        BillDetails billDetails = rideService.generateBill(rideId).orElse(null);

        if (billDetails == null) {
            logger.warn("Invalid or incomplete ride.");
            System.out.println("Invalid or incomplete ride.");
        } else {
            logger.info(
                    "Total Bill for Ride ID {} with Driver ID {} is {}",
                    billDetails.getRideId(),
                    billDetails.getDriverId(),
                    billDetails.getTotalFare()
            );

            System.out.printf(
                    "Total Bill for Ride ID %s with Driver ID %s is %.2f%n",
                    billDetails.getRideId(),
                    billDetails.getDriverId(),
                    billDetails.getTotalFare()
            );
        }
    }
}
