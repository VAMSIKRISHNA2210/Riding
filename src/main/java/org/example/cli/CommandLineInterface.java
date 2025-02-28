package org.example.cli;

import org.example.model.Ride;
import org.example.service.BillService;
import org.example.service.RideService;

import java.util.Scanner;

public class CommandLineInterface {

    private final RideService rideService;
    private final BillService billService;

    public CommandLineInterface(RideService rideService, BillService billService) {
        this.rideService = rideService;
        this.billService = billService;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ride Management System!");
        System.out.println("Enter 'EXIT' to quit the application.");

        while (true) {
            try {
                System.out.println("\nEnter command:");
                String command = scanner.nextLine();

                if ("EXIT".equalsIgnoreCase(command.trim())) {
                    System.out.println("Exiting application. Goodbye!");
                    break;
                }

                String[] parts = command.split(" ");
                if (parts.length == 0) {
                    System.out.println("Invalid command. Please try again.");
                    continue;
                }

                switch (parts[0].toUpperCase()) {
                    case "ADD_DRIVER":
                        handleAddDriver(parts);
                        break;

                    case "ADD_RIDER":
                        handleAddRider(parts);
                        break;

                    case "START_RIDE":
                        handleStartRide(parts);
                        break;

                    case "STOP_RIDE":
                        handleStopRide(parts);
                        break;

                    case "GENERATE_BILL":
                        handleGenerateBill(parts);
                        break;

                    default:
                        System.out.println("Unknown command. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void handleAddDriver(String[] parts) {
        if (parts.length != 5) {
            System.out.println("Error: ADD_DRIVER requires 4 arguments (id name latitude longitude)");
            return;
        }

        try {
            String id = parts[1];
            String name = parts[2];
            double latitude = Double.parseDouble(parts[3]);
            double longitude = Double.parseDouble(parts[4]);

            rideService.addDriver(id, name, latitude, longitude);
            System.out.println("Driver added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Latitude and longitude must be valid numbers.");
        }
    }

    private void handleAddRider(String[] parts) {
        if (parts.length != 5) {
            System.out.println("Error: ADD_RIDER requires 4 arguments (id name latitude longitude)");
            return;
        }

        try {
            String id = parts[1];
            String name = parts[2];
            double latitude = Double.parseDouble(parts[3]);
            double longitude = Double.parseDouble(parts[4]);

            rideService.addRider(id, name, latitude, longitude);
            System.out.println("Rider added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Latitude and longitude must be valid numbers.");
        }
    }

    private void handleStartRide(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Error: START_RIDE requires 3 arguments (rideId riderId driverId)");
            return;
        }

        try {
            String rideId = parts[1];
            String riderId = parts[2];
            String driverId = parts[3];

            rideService.startRide(rideId, riderId, driverId);
            System.out.println("Ride started successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleStopRide(String[] parts) {
        if (parts.length != 5) {
            System.out.println("Error: STOP_RIDE requires 4 arguments (rideId endLatitude endLongitude duration)");
            return;
        }

        try {
            String rideId = parts[1];
            double endLatitude = Double.parseDouble(parts[2]);
            double endLongitude = Double.parseDouble(parts[3]);
            int duration = Integer.parseInt(parts[4]);

            rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            System.out.println("Ride stopped successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Latitude, longitude, and duration must be valid numbers.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleGenerateBill(String[] parts) {
        if (parts.length != 2) {
            System.out.println("Error: GENERATE_BILL requires 1 argument (rideId)");
            return;
        }

        try {
            String rideId = parts[1];

            // Fetch the ride from RideService
            Ride ride = rideService.getRides().get(rideId);
            if (ride == null || !ride.isCompleted()) {
                System.out.println("Error: Invalid or incomplete ride.");
                return;
            }

            // Generate bill using BillService
            billService.generateBill(ride);
            System.out.println("Bill generated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
