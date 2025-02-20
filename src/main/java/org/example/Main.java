package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        RideService rideService = new RideService();
        BillService billService = new BillService();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String[] command = scanner.nextLine().split(" ");
            switch (command[0]) {
                case "ADD_DRIVER":
                    rideService.addDriver(command[1], Double.parseDouble(command[3]), Double.parseDouble(command[4]));
                    System.out.println("DRIVER_ADDED");
                    break;
                case "ADD_RIDER":
                    rideService.addRider(command[1], Double.parseDouble(command[3]), Double.parseDouble(command[4]));
                    System.out.println("RIDER_ADDED");
                    break;
                case "MATCH":
                    List<Driver> drivers = rideService.matchDrivers(command[1]);
                    if (drivers.isEmpty()) {
                        System.out.println("NO_DRIVERS_AVAILABLE");
                    } else {
                        System.out.print("DRIVERS_MATCHED");
                        for (Driver driver : drivers) {
                            System.out.print(" " + driver.getId());
                        }
                        System.out.println();
                    }
                    break;
                case "START_RIDE":
                    Ride ride = rideService.startRide(command[1], command[2], command[3]);
                    if (ride != null) {
                        System.out.println("RIDE_STARTED " + command[1]);
                    }
                    break;
                case "STOP_RIDE":
                    rideService.stopRide(command[1]);
                    break;
                case "BILL":
                    billService.generateBill(rideService.getRides().get(command[1]));
                    break;
                case "RATE_DRIVER":
                    rideService.rateDriver(command[1], Integer.parseInt(command[2]));
                    break;
                case "ADD_PREFERRED_DRIVER":
                    rideService.addPreferredDriver(command[1], command[2]);
                    break;
                default:
                    System.out.println("INVALID_COMMAND");
            }
        }
        scanner.close();
    }
}
