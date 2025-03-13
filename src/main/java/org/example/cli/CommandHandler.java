package org.example.cli;

import org.example.service.BillDetails;
import org.example.service.RideService;

/**
 * Handles commands for the CLI application.
 */
public class CommandHandler {
    private final RideService rideService;

    public CommandHandler(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Adds a new driver to the system.
     *
     * @param parts Command parts containing driver details.
     */
    public void addDriver(String[] parts) {
        rideService.addDriver(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        System.out.println("Driver added successfully");
    }

    /**
     * Adds a new rider to the system.
     *
     * @param parts Command parts containing rider details.
     */
    public void addRider(String[] parts) {
        rideService.addRider(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        System.out.println("Rider added successfully");
    }

    /**
     * Matches a rider with available drivers.
     *
     * @param parts Command parts containing rider ID.
     */
    public void matchRider(String[] parts) {
        System.out.println(String.join(" ", rideService.matchRider(parts[1])));
    }

    /**
     * Starts a new ride.
     *
     * @param parts Command parts containing ride details.
     */
    public void startRide(String[] parts) {
        try {
            String result = rideService.startRide(parts[1], Integer.parseInt(parts[2]), parts[3]);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Stops an ongoing ride.
     *
     * @param parts Command parts containing ride stop details.
     */
    public void stopRide(String[] parts) {
        try {
            String result = rideService.stopRide(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param parts Command parts containing ride ID.
     */
    public void generateBill(String[] parts) {
        BillDetails billDetails = rideService.generateBill(parts[1]).orElse(null);
        if (billDetails == null) {
            System.out.println("Invalid or incomplete ride");
            return;
        }
        System.out.printf("Total Bill: %s %s %.2f%n", billDetails.getRideId(), billDetails.getDriverId(), billDetails.getTotalFare());
    }
}
