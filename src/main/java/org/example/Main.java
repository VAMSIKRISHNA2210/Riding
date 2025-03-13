package org.example;

import org.example.cli.CommandHandler;
import org.example.service.RideService;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Main class for the ride application.
 * Provides a command-line interface for interacting with the RideService.
 */
public class Main {
    private static final RideService rideService = new RideService();
    private static final CommandHandler commandHandler = new CommandHandler(rideService);

    /**
     * Enum representing valid commands for the application, with associated actions.
     */
    private enum Command {
        ADD_DRIVER(commandHandler::addDriver),
        ADD_RIDER(commandHandler::addRider),
        MATCH(commandHandler::matchRider),
        START_RIDE(commandHandler::startRide),
        STOP_RIDE(commandHandler::stopRide),
        BILL(commandHandler::generateBill),
        EXIT(parts -> {}),
        INVALID(parts -> System.out.println("INVALID_COMMAND")); // Default behavior for invalid commands

        private final Consumer<String[]> action;

        Command(Consumer<String[]> action) {
            this.action = action;
        }

        /**
         * Executes the associated action for the command.
         *
         * @param parts The command arguments.
         */
        public void execute(String[] parts) {
            action.accept(parts);
        }

        /**
         * Parses a string into a Command enum value.
         *
         * @param command The string representation of the command.
         * @return The corresponding Command enum value, or INVALID if the input is invalid.
         */
        public static Command fromString(String command) {
            try {
                return Command.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                return INVALID; // Default to INVALID for unrecognized commands
            }
        }
    }

    /**
     * Entry point of the application.
     * Reads commands from standard input and processes them until EXIT is entered.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String commandLine = scanner.nextLine();
                processCommand(commandLine);
            }
        }
    }

    /**
     * Processes a single command by parsing it and executing the appropriate handler method.
     *
     * @param commandLine The command string to process.
     */
    private static void processCommand(String commandLine) {
        String[] parts = commandLine.split(" ");
        Command command = Command.fromString(parts[0]);

        if (command == Command.EXIT) return; // Exit application

        try {
            command.execute(parts);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
