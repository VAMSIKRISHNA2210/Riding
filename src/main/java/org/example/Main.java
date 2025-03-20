package org.example;

import org.example.cli.CommandHandler;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Main application class that handles the command-line interface.
 * This class is responsible for parsing user input, identifying commands,
 * and delegating their execution to the appropriate handlers.

 * The application runs in a continuous loop, processing commands until
 * the user explicitly quits or the input stream ends.
 */
@Component
public class Main {

    private final CommandHandler commandHandler;
    private boolean running = true;

    /**
     * Enum representing all supported commands in the application.
     * Each command corresponds to a specific action that can be performed.
     */
    public enum Command {
        ADD_DRIVER,    // Add a new driver to the system
        ADD_RIDER,     // Add a new rider to the system
        MATCH,         // Match a rider with available drivers
        START_RIDE,    // Start a new ride
        STOP_RIDE,     // Stop an ongoing ride
        BILL,          // Generate a bill for a completed ride
        QUIT,          // Exit the application
        INVALID        // Represents an unrecognized command
    }

    /**
     * Constructs a new Main instance with the specified command handler.
     *
     * @param commandHandler The handler responsible for executing commands
     */
    public Main(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Starts the application's main loop.
     * Continuously reads input from the console, parses commands,
     * and processes them until the application is terminated.
     */
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                Command command = parseCommand(input);
                processCommand(command, input);
            }
        }
    }

    /**
     * Parses the input string to identify the command.
     * The command is expected to be the first word of the input.
     *
     * @param input The raw input string from the user
     * @return The identified Command enum value, or INVALID if not recognized
     */
    private Command parseCommand(String input) {
        String commandString = input.split(" ")[0].toUpperCase();
        try {
            return Command.valueOf(commandString);
        } catch (IllegalArgumentException e) {
            return Command.INVALID;
        }
    }

    /**
     * Processes the identified command by delegating to the appropriate handler method.
     * Handles exceptions that might occur during command execution.
     *
     * @param command The identified command to process
     * @param input The original input string containing the command and its parameters
     */
    private void processCommand(Command command, String input) {
        try {
            String[] parts = input.split(" ");
            switch (command) {
                case ADD_DRIVER -> commandHandler.addDriver(parts);
                case ADD_RIDER -> commandHandler.addRider(parts);
                case MATCH -> commandHandler.matchRider(parts);
                case START_RIDE -> commandHandler.startRide(parts);
                case STOP_RIDE -> commandHandler.stopRide(parts);
                case BILL -> commandHandler.generateBill(parts);
                case QUIT -> {
                    System.out.println("Exiting the application.");
                    running = false;
                }
                case INVALID -> System.out.println("INVALID_COMMAND");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid parameters entered: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
