package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing ride operations.
 */
@Tag(name = "Ride Management", description = "APIs for managing drivers, riders, and ride operations")
@RestController
@RequestMapping("/api")
public class RideController {
    private static final Logger logger = LoggerFactory.getLogger(RideController.class);
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @Operation(
            summary = "Add a new driver",
            description = "Registers a new driver with location coordinates"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @PostMapping("/drivers")
    public ResponseEntity<String> addDriver(
            @Parameter(description = "Driver's unique identifier", required = true) @RequestParam String id,
            @Parameter(description = "Initial latitude coordinate", required = true) @RequestParam double latitude,
            @Parameter(description = "Initial longitude coordinate", required = true) @RequestParam double longitude) {

        logger.info("Received request to add driver with ID: {}", id);
        try {
            rideService.addDriver(id, latitude, longitude);
            return ResponseEntity.ok("Driver added successfully.");
        } catch (Exception e) {
            logger.error("Error adding driver: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: Unable to add driver.");
        }
    }

    @Operation(
            summary = "Add a new rider",
            description = "Registers a new rider with location coordinates"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rider added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters")
    })
    @PostMapping("/riders")
    public ResponseEntity<String> addRider(
            @Parameter(description = "Rider's unique identifier", required = true) @RequestParam String id,
            @Parameter(description = "Initial latitude coordinate", required = true) @RequestParam double latitude,
            @Parameter(description = "Initial longitude coordinate", required = true) @RequestParam double longitude) {

        logger.info("Received request to add rider with ID: {}", id);
        try {
            rideService.addRider(id, latitude, longitude);
            return ResponseEntity.ok("Rider added successfully.");
        } catch (Exception e) {
            logger.error("Error adding rider: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: Unable to add rider.");
        }
    }

    @Operation(
            summary = "Match rider to drivers",
            description = "Finds available drivers near the specified rider"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of matched driver IDs"),
            @ApiResponse(responseCode = "400", description = "Invalid rider ID")
    })
    @GetMapping("/match/{riderId}")
    public ResponseEntity<?> matchRider(
            @Parameter(description = "ID of the rider to match", required = true) @PathVariable String riderId) {

        logger.info("Matching drivers for rider: {}", riderId);
        try {
            List<String> matches = rideService.matchRider(riderId);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            logger.error("Matching error: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: Unable to match drivers.");
        }
    }

    @Operation(
            summary = "Start a new ride",
            description = "Initiates a new ride with selected driver"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride started successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ride parameters")
    })
    @PostMapping("/start")
    public ResponseEntity<String> startRide(
            @Parameter(description = "Unique ride ID", required = true) @RequestParam String rideId,
            @Parameter(description = "Driver selection index", required = true) @RequestParam int n,
            @Parameter(description = "Rider ID", required = true) @RequestParam String riderId) {

        logger.info("Starting ride: {}", rideId);
        try {
            String result = rideService.startRide(rideId, n, riderId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Stop an ongoing ride",
            description = "Ends a ride and calculates fare"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride stopped successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ride parameters")
    })
    @PostMapping("/stop")
    public ResponseEntity<String> stopRide(
            @Parameter(description = "Ride ID to stop", required = true) @RequestParam String rideId,
            @Parameter(description = "End latitude", required = true) @RequestParam double endLatitude,
            @Parameter(description = "End longitude", required = true) @RequestParam double endLongitude,
            @Parameter(description = "Ride duration in minutes", required = true) @RequestParam double duration) {

        logger.info("Stopping ride: {}", rideId);
        try {
            String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Generate ride bill",
            description = "Generates fare details for completed ride"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bill generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ride ID")
    })
    @GetMapping("/bill/{rideId}")
    public ResponseEntity<?> generateBill(
            @Parameter(description = "Ride ID to bill", required = true) @PathVariable String rideId) {

        logger.info("Generating bill for ride: {}", rideId);
        try {
            return rideService.generateBill(rideId)
                    .map(billDetails -> ResponseEntity.ok(String.format(
                            "Total Bill for Ride ID %s with Driver ID %s is %.2f",
                            billDetails.getRideId(),
                            billDetails.getDriverId(),
                            billDetails.getTotalFare())))
                    .orElseGet(() -> ResponseEntity.badRequest().body("Invalid or incomplete ride."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating bill.");
        }
    }
}
