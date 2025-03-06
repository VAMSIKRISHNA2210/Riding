package org.example;

import org.example.service.RideService;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main class for the ride application.
 * Provides a command-line interface for interacting with the RideService.
 */
public class Main {
    private static final RideService rideService = new RideService();
    private static final Map<String, Consumer<String[]>> commandMap = new HashMap<>();

    static {
        commandMap.put("ADD_DRIVER", Main::addDriver);
        commandMap.put("ADD_RIDER", Main::addRider);
        commandMap.put("MATCH", Main::matchRider);
        commandMap.put("START_RIDE", Main::startRide);
        commandMap.put("STOP_RIDE", Main::stopRide);
        commandMap.put("BILL", Main::generateBill);
    }

    /**
     * Entry point of the application.
     * Reads commands from standard input and processes them until EXIT is entered.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("EXIT")) {
                    break;
                }
                processCommand(command);
            }
        }
    }

    /**
     * Processes a single command by splitting it into parts and executing the appropriate handler.
     *
     * @param command the command string to process
     */
    private static void processCommand(String command) {
        String[] parts = command.split(" ");
        Consumer<String[]> commandHandler = commandMap.get(parts[0]);
        if (commandHandler != null) {
            try {
                commandHandler.accept(parts);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("INVALID_COMMAND");
        }
    }

    /**
     * Adds a new driver to the system.
     *
     * @param parts command parts containing driver details
     */
    private static void addDriver(String[] parts) {
        rideService.addDriver(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        System.out.println("Driver added successfully");
    }

    /**
     * Adds a new rider to the system.
     *
     * @param parts command parts containing rider details
     */
    private static void addRider(String[] parts) {
        rideService.addRider(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        System.out.println("Rider added successfully");
    }

    /**
     * Matches a rider with available drivers.
     *
     * @param parts command parts containing rider ID
     */
    private static void matchRider(String[] parts) {
        System.out.println(String.join(" ", rideService.matchRider(parts[1])));
    }

    /**
     * Starts a new ride.
     *
     * @param parts command parts containing ride details
     */
    private static void startRide(String[] parts) {
        System.out.println(rideService.startRide(parts[1], Integer.parseInt(parts[2]), parts[3]));
    }

    /**
     * Stops an ongoing ride.
     *
     * @param parts command parts containing ride stop details
     */
    private static void stopRide(String[] parts) {
        System.out.println(rideService.stopRide(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4])));
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param parts command parts containing ride ID
     */
    private static void generateBill(String[] parts) {
        System.out.println(rideService.generateBillForCli(parts[1]));
    }
}
