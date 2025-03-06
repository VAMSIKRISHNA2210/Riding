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

    /**
     * Constructs a new RideController with the given RideService.
     *
     * @param rideService the service to handle ride-related operations
     */
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Adds a new driver to the system.
     *
     * @param id        the driver's unique identifier
     * @param latitude  the driver's initial latitude
     * @param longitude the driver's initial longitude
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
     * @param id        the rider's unique identifier
     * @param latitude  the rider's initial latitude
     * @param longitude the rider's initial longitude
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
     * @param riderId the ID of the rider to match
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
     * @param rideId  the unique identifier for the ride
     * @param n       the index of the chosen driver from the matched list
     * @param riderId the ID of the rider starting the ride
     * @return ResponseEntity with a success message
     */
    @PostMapping("/start")
    public ResponseEntity<String> startRide(@RequestParam String rideId,
                                            @RequestParam int n,
                                            @RequestParam String riderId) {
        String result = rideService.startRide(rideId, n, riderId);
        if (result.equals("INVALID_RIDE")) {
            return ResponseEntity.badRequest().body("Invalid ride or rider ID");
        } else {
            return ResponseEntity.ok(result);
        }
    }


    /**
     * Stops an ongoing ride.
     *
     * @param rideId       the ID of the ride to stop
     * @param endLatitude  the end latitude of the ride
     * @param endLongitude the end longitude of the ride
     * @param duration     the duration of the ride in minutes
     * @return ResponseEntity with the result of stopping the ride
     */
    @PostMapping("/stop")
    public ResponseEntity<String> stopRide(@RequestParam String rideId,
                                           @RequestParam double endLatitude,
                                           @RequestParam double endLongitude,
                                           @RequestParam double duration) {
        String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);
        if (result.equals("INVALID_RIDE")) {
            return ResponseEntity.badRequest().body("Invalid ride");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    /**
     * Generates a bill for a completed ride.
     *
     * @param rideId the ID of the ride to generate the bill for
     * @return ResponseEntity with the generated bill
     */
    @GetMapping("/bill/{rideId}")
    public ResponseEntity<String> generateBill(@PathVariable String rideId) {
        String bill = rideService.generateBillForApi(rideId);
        return ResponseEntity.ok(bill);
    }
}
