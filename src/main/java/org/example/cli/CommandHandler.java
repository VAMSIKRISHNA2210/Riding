package org.example.cli;

import org.example.service.BillDetails;
import org.example.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private final RideService rideService;

    public CommandHandler(RideService rideService) {
        this.rideService = rideService;
    }

    public void addDriver(String id, double latitude, double longitude) {
        try {
            rideService.addDriver(id, latitude, longitude);
            logger.info("Driver added successfully. Driver ID: {}", id);
            System.out.println("Driver added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addRider(String id, double latitude, double longitude) {
        try {
            rideService.addRider(id, latitude, longitude);
            logger.info("Rider added successfully. Rider ID: {}", id);
            System.out.println("Rider added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void matchRider(String riderId) {
        List<String> matches = rideService.matchRider(riderId);
        if (matches.isEmpty()) {
            logger.warn("No drivers matched for Rider ID: {}", riderId);
            System.out.println("No drivers matched for Rider ID: " + riderId);
        } else {
            logger.info("Matched drivers for Rider ID {}: {}", riderId, String.join(", ", matches));
            System.out.println("Matched drivers: " + String.join(", ", matches));
        }
    }

    public void startRide(String rideId, int driverIndex, String riderId) {
        try {
            String result = rideService.startRide(rideId, driverIndex, riderId);
            logger.info("Ride started successfully. Ride ID: {}", result);
            System.out.println("Ride started successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Error starting ride: {}", e.getMessage());
            System.out.println("Error starting ride: " + e.getMessage());
        }
    }

    public void stopRide(String rideId, double endLatitude, double endLongitude, double duration) {
        try {
            String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            logger.info("Ride stopped successfully. Ride ID: {}", result);
            System.out.println("Ride stopped successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Error stopping ride: {}", e.getMessage());
            System.out.println("Error stopping ride: " + e.getMessage());
        }
    }

    public void generateBill(String rideId) {
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
