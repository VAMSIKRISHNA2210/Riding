package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Main Spring Boot application class that serves as the entry point for the application.
 * This class initializes the Spring application context and determines whether to run
 * in REST API mode (default) or CLI mode based on command-line arguments.
 */
@SpringBootApplication
public class RiderApplication {

    /**
     * The main method that starts the Spring Boot application.
     * If "cli" is provided as the first command-line argument, the application
     * will run in CLI mode; otherwise, it will run as a REST API server.
     *
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RiderApplication.class, args);
        if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
            runCliMode(context);
        }
    }

    /**
     * Runs the application in CLI (Command Line Interface) mode.
     * This method retrieves the Main bean from the Spring context and
     * starts its command processing loop.
     *
     * @param context The Spring application context
     */
    public static void runCliMode(ApplicationContext context) {
        RiderCli main = context.getBean(RiderCli.class);
        main.run();
    }
}
