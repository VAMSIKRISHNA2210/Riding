package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the REST API application.
 * This class initializes and runs the Spring Boot application.
 */
@SpringBootApplication
public class RestApiApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }
}
