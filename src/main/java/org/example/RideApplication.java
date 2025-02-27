package org.example;

import org.example.cli.CommandLineInterface;
import org.example.service.BillService;
import org.example.service.RideService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RideApplication {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("rest")) {
            // Start REST API mode
            SpringApplication.run(RideApplication.class, args);
        } else {
            // Start CLI mode
            RideService rideService = new RideService();
            BillService billService = new BillService();
            CommandLineInterface cli = new CommandLineInterface(rideService, billService);
            cli.run();
        }
    }
}