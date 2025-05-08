package org.example;

import org.example.cli.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main application class that handles the command-line interface.
 * This class is responsible for parsing user input, identifying commands,
 * and delegating their execution to the appropriate handlers.

 * The application runs in a continuous loop, processing commands until
 * the user explicitly quits or the input stream ends.
 */
@Component
public class RiderCli {
    private final Map<String, Command> commandMap = new HashMap<>();
    private boolean running = true;

    public RiderCli(CommandHandler commandHandler) {
        commandMap.put("ADD_DRIVER", new AddDriverCommand(commandHandler));
        commandMap.put("ADD_RIDER", new AddRiderCommand(commandHandler));
        commandMap.put("MATCH", new MatchCommand(commandHandler));
        commandMap.put("START_RIDE", new StartRideCommand(commandHandler));
        commandMap.put("STOP_RIDE", new StopRideCommand(commandHandler));
        commandMap.put("BILL", new BillCommand(commandHandler));
        commandMap.put("QUIT", new QuitCommand(this));
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                String[] parts = input.split(" ");
                String commandKey = parts[0].toUpperCase();
                Command command = commandMap.getOrDefault(commandKey, new InvalidCommand());
                command.setArgs(parts);
                try {
                    command.execute();
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid parameters: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error executing command: " + e.getMessage());
                }
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
