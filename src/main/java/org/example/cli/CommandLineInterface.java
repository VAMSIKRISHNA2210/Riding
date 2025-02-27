package org.example.cli;

import org.example.service.RideService;
import org.example.service.BillService;

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

        while (true) {
            try {
                System.out.println("Enter command (or type 'EXIT' to quit):");
                String command = scanner.nextLine();

                if ("EXIT".equalsIgnoreCase(command.trim())) {
                    System.out.println("Exiting application. Goodbye!");
                    break; // Exit the loop
                }

                String[] parts = command.split(" ");

                switch (parts[0].toUpperCase()) {
                    case "ADD_DRIVER":
                        if (parts.length != 5) {
                            System.out.println("Error: ADD_DRIVER requires 4 arguments (id, name, latitude, longitude)");
                            break;
                        }
                        rideService.addDriver(parts[1], parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
                        System.out.println("Driver added successfully");
                        break;

                    case "ADD_RIDER":
                        if (parts.length != 5) {
                            System.out.println("Error: ADD_RIDER requires 4 arguments (id, name, latitude, longitude)");
                            break;
                        }
                        rideService.addRider(parts[1], parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
                        System.out.println("Rider added successfully");
                        break;

                    case "START_RIDE":
                        if (parts.length != 4) {
                            System.out.println("Error: START_RIDE requires 3 arguments (rideId, riderId, driverId)");
                            break;
                        }
                        rideService.startRide(parts[1], parts[2], parts[3]);
                        System.out.println("Ride started successfully");
                        break;

                    case "STOP_RIDE":
                        if (parts.length != 5) {
                            System.out.println("Error: STOP_RIDE requires 4 arguments (rideId, endLatitude, endLongitude, duration)");
                            break;
                        }
                        rideService.stopRide(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
                        System.out.println("Ride stopped successfully");
                        break;

                    case "GENERATE_BILL":
                        if (parts.length != 2) {
                            System.out.println("Error: GENERATE_BILL requires 1 argument (rideId)");
                            break;
                        }
                        billService.generateBill(rideService.getRides().get(parts[1]));
                        break;

                    default:
                        System.out.println("Invalid command");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}