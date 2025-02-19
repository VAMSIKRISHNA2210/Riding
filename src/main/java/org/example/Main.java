package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RideService rideService = new RideService();
        BillService billService = new BillService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter commands (type 'EXIT' to quit):");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("EXIT")) {
                break;
            }

            String[] command = input.split(" ");
            switch (command[0]) {
                case "ADD_DRIVER":
                    if (command.length != 5) {
                        System.out.println("INVALID_COMMAND");
                        break;
                    }
                    String driverId = command[1];
                    String driverName = command[2];
                    double driverLat = Double.parseDouble(command[3]);
                    double driverLong = Double.parseDouble(command[4]);
                    rideService.addDriver(driverId, driverName, driverLat, driverLong);
                    System.out.println("DRIVER_ADDED: " + driverId);
                    break;

                case "ADD_RIDER":
                    if (command.length != 5) {
                        System.out.println("INVALID_COMMAND");
                        break;
                    }
                    String riderId = command[1];
                    String riderName = command[2];
                    double riderLat = Double.parseDouble(command[3]);
                    double riderLong = Double.parseDouble(command[4]);
                    rideService.addRider(riderId, riderName, riderLat, riderLong);
                    System.out.println("RIDER_ADDED: " + riderId);
                    break;

                case "MATCH":
                    if (command.length != 2) {
                        System.out.println("INVALID_COMMAND");
                        break;
                    }
                    String matchRiderId = command[1];
                    List<Driver> matchedDrivers = rideService.matchDrivers(matchRiderId);
                    if (matchedDrivers.isEmpty()) {
                        System.out.println("NO_DRIVERS_AVAILABLE");
                    } else {
                        System.out.print("MATCHED_DRIVERS:");
                        for (Driver driver : matchedDrivers) {
                            System.out.print(" " + driver.getId());
                        }
                        System.out.println();
                    }
                    break;

                case "START_RIDE":
                    if (command.length != 4) {
                        System.out.println("INVALID_COMMAND");
                        break;
                    }
                    String rideId = command[1];
                    String startRiderId = command[2];
                    String startDriverId = command[3];
                    Ride ride = rideService.startRide(rideId, startRiderId, startDriverId);
                    if (ride != null) {
                        System.out.println("RIDE_STARTED: " + rideId);
                    }
                    break;

                case "STOP_RIDE":
                    if (command.length != 5) {
                        System.out.println("INVALID_COMMAND");
                        break;
                    }
                    String stopRideId = command[1];
                    double endLat = Double.parseDouble(command[2]);
                    double endLong = Double.parseDouble(command[3]);
                    int duration = Integer.parseInt(command[4]);
                    rideService.stopRide(stopRideId, endLat, endLong, duration);
                    break;

                case "BILL":
                    if (command.length != 2) {
                        System.out.println("INVALID_COMMAND");
                        break;
                    }
                    String billRideId = command[1];
                    Ride completedRide = rideService.getRides().get(billRideId);
                    billService.generateBill(completedRide);
                    break;

                default:
                    System.out.println("UNKNOWN_COMMAND");
            }
        }

        scanner.close();
        System.out.println("Program terminated.");
    }
}
