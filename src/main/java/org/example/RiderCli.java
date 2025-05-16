package org.example;

import org.example.cli.*;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class RiderCli {
    private final CommandHandler commandHandler;
    private boolean running = true;

    public RiderCli(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                String input = scanner.nextLine().trim();
                String[] parts = input.split(" ");
                String commandKey = parts[0].toUpperCase();

                try {
                    Command command = createCommand(commandKey, parts);
                    command.execute();
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid parameters." + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error executing command." + e.getMessage());
                }
            }
        }
    }

    private Command createCommand(String commandKey, String[] parts) {
        return switch (commandKey) {
            case "ADD_DRIVER" -> {
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Usage: ADD_DRIVER <id> <latitude> <longitude>");
                }
                yield new AddDriverCommand(
                        commandHandler,
                        parts[1],
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3])
                );
            }
            case "ADD_RIDER" -> {
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Usage: ADD_RIDER <id> <latitude> <longitude>");
                }
                yield new AddRiderCommand(
                        commandHandler,
                        parts[1],
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3])
                );
            }
            case "MATCH" -> {
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Usage: MATCH <rider_id>");
                }
                yield new MatchCommand(commandHandler, parts[1]);
            }
            case "START_RIDE" -> {
                if (parts.length != 4) {
                    throw new IllegalArgumentException("Usage: START_RIDE <ride_id> <driver_index> <rider_id>");
                }
                yield new StartRideCommand(
                        commandHandler,
                        parts[1],
                        Integer.parseInt(parts[2]),
                        parts[3]
                );
            }
            case "STOP_RIDE" -> {
                if (parts.length != 5) {
                    throw new IllegalArgumentException("Usage: STOP_RIDE <ride_id> <end_latitude> <end_longitude> <duration>");
                }
                yield new StopRideCommand(
                        commandHandler,
                        parts[1],
                        Double.parseDouble(parts[2]),
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4])
                );
            }
            case "BILL" -> {
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Usage: BILL <ride_id>");
                }
                yield new BillCommand(commandHandler, parts[1]);
            }
            case "QUIT" -> new QuitCommand(this);
            default -> new InvalidCommand();
        };
    }

    public void stop() {
        this.running = false;
    }
}
