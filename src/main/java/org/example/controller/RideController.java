package org.example.controller;

import org.example.service.RideService;
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
     * @return ResponseEntity with a success message
     */
    @PostMapping("/drivers/add")
    public ResponseEntity<String> addDriver(@RequestParam String id,
                                            @RequestParam double latitude,
                                            @RequestParam double longitude) {
        rideService.addDriver(id, latitude, longitude);
        return ResponseEntity.ok("Driver added successfully");
    }

    /**
     * Adds a new rider to the system.
     *
     * @param id        The rider's unique identifier
     * @param latitude  The rider's initial latitude
     * @param longitude The rider's initial longitude
     * @return ResponseEntity with a success message
     */
    @PostMapping("/riders/add")
    public ResponseEntity<String> addRider(@RequestParam String id,
                                           @RequestParam double latitude,
                                           @RequestParam double longitude) {
        rideService.addRider(id, latitude, longitude);
        return ResponseEntity.ok("Rider added successfully");
    }

    /**
     * Matches a rider with nearby drivers.
     *
     * @param riderId The ID of the rider to match
     * @return ResponseEntity with a list of matched driver IDs
     */
    @GetMapping("/match/{riderId}")
    public ResponseEntity<List<String>> matchRider(@PathVariable String riderId) {
        List<String> matches = rideService.matchRider(riderId);
        return ResponseEntity.ok(matches);
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
        try {
            String result = rideService.startRide(rideId, n, riderId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
        try {
            String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
        return rideService.generateBill(rideId)
                .map(billDetails -> {
                    String formattedBill = String.format("BILL %s %s %.2f",
                            billDetails.getRideId(),
                            billDetails.getDriverId(),
                            billDetails.getTotalFare());
                    return ResponseEntity.ok(formattedBill);
                })
                .orElse(ResponseEntity.badRequest().body("Invalid or incomplete ride"));
    }
}
