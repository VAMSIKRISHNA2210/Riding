package org.example.controller;

import org.example.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing ride operations.
 * This class handles HTTP requests related to drivers, riders, and rides.
 */
@RestController
@RequestMapping("/api/rides")
public class RideController {
    private static final Logger logger = LoggerFactory.getLogger(RideController.class); // SLF4J Logger
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Adds a new driver to the system.
     *
     * @param id        The driver's unique identifier
     * @param latitude  The driver's initial latitude
     * @param longitude The driver's initial longitude
     * @return ResponseEntity with a success message or an error message
     */
    @PostMapping("/drivers/add")
    public ResponseEntity<String> addDriver(@RequestParam String id,
                                            @RequestParam double latitude,
                                            @RequestParam double longitude) {
        logger.info("Received request to add driver with ID: {}", id);
        try {
            rideService.addDriver(id, latitude, longitude);
            logger.info("Driver added successfully. Driver ID: {}", id);
            return ResponseEntity.ok("Driver added successfully.");
        } catch (Exception e) {
            logger.error("Error while adding driver with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: Unable to add driver.");
        }
    }

    /**
     * Adds a new rider to the system.
     *
     * @param id        The rider's unique identifier
     * @param latitude  The rider's initial latitude
     * @param longitude The rider's initial longitude
     * @return ResponseEntity with a success message or an error message
     */
    @PostMapping("/riders/add")
    public ResponseEntity<String> addRider(@RequestParam String id,
                                           @RequestParam double latitude,
                                           @RequestParam double longitude) {
        logger.info("Received request to add rider with ID: {}", id);
        try {
            rideService.addRider(id, latitude, longitude);
            logger.info("Rider added successfully. Rider ID: {}", id);
            return ResponseEntity.ok("Rider added successfully.");
        } catch (Exception e) {
            logger.error("Error while adding rider with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: Unable to add rider.");
        }
    }

    /**
     * Matches a rider with nearby drivers.
     *
     * @param riderId The ID of the rider to match
     * @return ResponseEntity with a list of matched driver IDs or an error message
     */
    @GetMapping("/match/{riderId}")
    public ResponseEntity<?> matchRider(@PathVariable String riderId) {
        logger.info("Received request to match drivers for Rider ID: {}", riderId);
        try {
            List<String> matches = rideService.matchRider(riderId);
            if (matches.isEmpty()) {
                logger.warn("No drivers matched for Rider ID: {}", riderId);
                return ResponseEntity.ok(matches); // Return empty list if no matches found
            }
            logger.info("Matched drivers for Rider ID {}: {}", riderId, matches);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            logger.error("Error while matching drivers for Rider ID {}: {}", riderId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: Unable to match drivers.");
        }
    }

    /**
     * Starts a new ride.
     *
     * @param rideId  The unique identifier for the ride
     * @param n       The index of the chosen driver from the matched list
     * @param riderId The ID of the rider starting the ride
     * @return ResponseEntity with the ride ID if successful or an error message if invalid
     */
    @PostMapping("/start")
    public ResponseEntity<String> startRide(@RequestParam String rideId,
                                            @RequestParam int n,
                                            @RequestParam String riderId) {
        logger.info("Received request to start ride with Ride ID: {}, Driver Index: {}, Rider ID: {}", rideId, n, riderId);
        try {
            String result = rideService.startRide(rideId, n, riderId);
            logger.info("Ride started successfully. Ride ID: {}", result);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to start ride with Ride ID {}: {}", rideId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while starting ride with Ride ID {}: {}", rideId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: Unable to start ride.");
        }
    }

    /**
     * Stops an ongoing ride.
     *
     * @param rideId       The ID of the ride to stop
     * @param endLatitude  The end latitude of the ride
     * @param endLongitude The end longitude of the ride
     * @param duration     The duration of the ride in minutes
     * @return ResponseEntity with the ride ID if successful or an error message if invalid
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopRide(@RequestParam String rideId,
                                           @RequestParam double endLatitude,
                                           @RequestParam double endLongitude,
                                           @RequestParam double duration) {
        logger.info("Received request to stop ride with Ride ID: {}, End Latitude: {}, End Longitude: {}, Duration: {}",
                rideId, endLatitude, endLongitude, duration);
        try {
            String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            logger.info("Ride stopped successfully. Ride ID: {}", result);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to stop ride with Ride ID {}: {}", rideId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while stopping ride with Ride ID {}: {}", rideId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: Unable to stop ride.");
        }
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param rideId The ID of the completed ride.
     * @return ResponseEntity containing formatted bill details or an error message if invalid.
     */
    @GetMapping("/bill/{rideId}")
    public ResponseEntity<?> generateBill(@PathVariable String rideId) {
        logger.info("Received request to generate bill for Ride ID: {}", rideId);
        try {
            return rideService.generateBill(rideId)
                    .map(billDetails -> {
                        String formattedBill = String.format(
                                "Total Bill for Ride ID %s with Driver ID %s is %.2f",
                                billDetails.getRideId(),
                                billDetails.getDriverId(),
                                billDetails.getTotalFare()
                        );
                        logger.info(formattedBill); // Log formatted bill details
                        return ResponseEntity.ok(formattedBill);
                    })
                    .orElseGet(() -> {
                        logger.warn("Failed to generate bill for Ride ID {}. Invalid or incomplete.", rideId);
                        return ResponseEntity.badRequest().body("Invalid or incomplete ride.");
                    });
        } catch (Exception e) {
            logger.error("Unexpected error while generating bill for Ride ID {}: {}", rideId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error: Unable to generate bill.");
        }
    }
}
