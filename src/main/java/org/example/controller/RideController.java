package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing ride operations following REST API guidelines.
 */
@Tag(name = "Ride Management", description = "RESTful APIs for managing drivers, riders, and ride operations")
@RestController
@RequestMapping("/api")
public class RideController {

    private static final Logger logger = LoggerFactory.getLogger(RideController.class);
    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Driver Resource Operations
    @Operation(
            summary = "Add a new driver",
            description = "Registers a new driver with location coordinates"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Driver created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "409", description = "Driver already exists")
    })
    @PostMapping("/drivers")
    public ResponseEntity<Map<String, Object>> createDriver(
            @Parameter(description = "Driver's unique identifier", required = true)
            @RequestParam String driverId,
            @Parameter(description = "Initial latitude coordinate", required = true)
            @RequestParam double latitude,
            @Parameter(description = "Initial longitude coordinate", required = true)
            @RequestParam double longitude) {

        logger.info("Received request to add driver with ID: {}", driverId);
        try {
            rideService.addDriver(driverId, latitude, longitude);

            Map<String, Object> response = Map.of(
                    "message", "Driver created successfully",
                    "driverId", driverId,
                    "location", Map.of(
                            "latitude", latitude,
                            "longitude", longitude
                    ),
                    "_links", List.of(
                            Map.of(
                                    "href", "/api/drivers/" + driverId,
                                    "rel", "self",
                                    "method", "GET"
                            )
                    )
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding driver: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Bad Request",
                    "message", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error adding driver: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Internal Server Error",
                    "message", "Unable to create driver",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Rider Resource Operations
    @Operation(
            summary = "Add a new rider",
            description = "Registers a new rider with location coordinates"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rider created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "409", description = "Rider already exists")
    })
    @PostMapping("/riders")
    public ResponseEntity<Map<String, Object>> createRider(
            @Parameter(description = "Rider's unique identifier", required = true)
            @RequestParam String riderId,
            @Parameter(description = "Initial latitude coordinate", required = true)
            @RequestParam double latitude,
            @Parameter(description = "Initial longitude coordinate", required = true)
            @RequestParam double longitude) {

        logger.info("Received request to add rider with ID: {}", riderId);
        try {
            rideService.addRider(riderId, latitude, longitude);

            Map<String, Object> response = Map.of(
                    "message", "Rider created successfully",
                    "riderId", riderId,
                    "location", Map.of(
                            "latitude", latitude,
                            "longitude", longitude
                    ),
                    "_links", List.of(
                            Map.of(
                                    "href", "/api/riders/" + riderId,
                                    "rel", "self",
                                    "method", "GET"
                            ),
                            Map.of(
                                    "href", "/api/riders/" + riderId + "/matches",
                                    "rel", "matches",
                                    "method", "GET"
                            )
                    )
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding rider: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Bad Request",
                    "message", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error adding rider: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Internal Server Error",
                    "message", "Unable to create rider",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Rider Matches Sub-resource
    @Operation(
            summary = "Get available drivers for rider",
            description = "Finds available drivers near the specified rider"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of matched driver IDs"),
            @ApiResponse(responseCode = "404", description = "Rider not found"),
            @ApiResponse(responseCode = "400", description = "Invalid rider ID")
    })
    @GetMapping("/riders/{riderId}/matches")
    public ResponseEntity<Map<String, Object>> getRiderMatches(
            @Parameter(description = "ID of the rider to match", required = true)
            @PathVariable String riderId,
            @Parameter(description = "Maximum distance radius for matching")
            @RequestParam(required = false) Double maxDistance,
            @Parameter(description = "Sort order for results")
            @RequestParam(required = false, defaultValue = "distance") String sort) {

        logger.info("Matching drivers for rider: {}", riderId);
        try {
            List<String> matches = rideService.matchRider(riderId);

            Map<String, Object> response = Map.of(
                    "riderId", riderId,
                    "availableDrivers", matches,
                    "count", matches.size(),
                    "filters", Map.of(
                            "maxDistance", maxDistance != null ? maxDistance : "default",
                            "sort", sort
                    ),
                    "_links", List.of(
                            Map.of(
                                    "href", "/api/riders/" + riderId + "/matches",
                                    "rel", "self",
                                    "method", "GET"
                            ),
                            Map.of(
                                    "href", "/api/rides",
                                    "rel", "create-ride",
                                    "method", "POST"
                            )
                    )
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid rider ID: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Not Found",
                    "message", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            logger.error("Matching error: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Internal Server Error",
                    "message", "Unable to match drivers",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Ride Resource Operations
    @Operation(
            summary = "Create a new ride",
            description = "Initiates a new ride with selected driver"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ride created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ride parameters"),
            @ApiResponse(responseCode = "409", description = "Ride already exists")
    })
    @PostMapping("/rides")
    public ResponseEntity<Map<String, Object>> createRide(
            @Parameter(description = "Unique ride ID", required = true)
            @RequestParam String rideId,
            @Parameter(description = "Driver selection index", required = true)
            @RequestParam int driverIndex,
            @Parameter(description = "Rider ID", required = true)
            @RequestParam String riderId) {

        logger.info("Creating ride: {}", rideId);
        try {
            String result = rideService.startRide(rideId, driverIndex, riderId);

            Map<String, Object> response = Map.of(
                    "message", "Ride created successfully",
                    "rideId", result,
                    "riderId", riderId,
                    "driverIndex", driverIndex,
                    "status", "active",
                    "_links", List.of(
                            Map.of(
                                    "href", "/api/rides/" + result,
                                    "rel", "self",
                                    "method", "GET"
                            ),
                            Map.of(
                                    "href", "/api/rides/" + result + "/complete",
                                    "rel", "complete",
                                    "method", "PATCH"
                            )
                    )
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating ride: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Bad Request",
                    "message", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error creating ride: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Internal Server Error",
                    "message", "Unable to create ride",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Complete a ride",
            description = "Ends a ride and calculates fare"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ride parameters"),
            @ApiResponse(responseCode = "404", description = "Ride not found")
    })
    @PatchMapping("/rides/{rideId}/complete")
    public ResponseEntity<Map<String, Object>> completeRide(
            @Parameter(description = "Ride ID to complete", required = true)
            @PathVariable String rideId,
            @Parameter(description = "End latitude", required = true)
            @RequestParam double endLatitude,
            @Parameter(description = "End longitude", required = true)
            @RequestParam double endLongitude,
            @Parameter(description = "Ride duration in minutes", required = true)
            @RequestParam double duration) {

        logger.info("Completing ride: {}", rideId);
        try {
            String result = rideService.stopRide(rideId, endLatitude, endLongitude, duration);

            Map<String, Object> response = Map.of(
                    "message", "Ride completed successfully",
                    "rideId", result,
                    "endLocation", Map.of(
                            "latitude", endLatitude,
                            "longitude", endLongitude
                    ),
                    "duration", duration,
                    "status", "completed",
                    "_links", List.of(
                            Map.of(
                                    "href", "/api/rides/" + result,
                                    "rel", "self",
                                    "method", "GET"
                            ),
                            Map.of(
                                    "href", "/api/rides/" + result + "/bill",
                                    "rel", "bill",
                                    "method", "GET"
                            )
                    )
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error completing ride: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Bad Request",
                    "message", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected error completing ride: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Internal Server Error",
                    "message", "Unable to complete ride",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(
            summary = "Get ride bill",
            description = "Generates fare details for completed ride"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bill generated successfully"),
            @ApiResponse(responseCode = "404", description = "Ride not found or incomplete"),
            @ApiResponse(responseCode = "400", description = "Invalid ride ID")
    })
    @GetMapping("/rides/{rideId}/bill")
    public ResponseEntity<Map<String, Object>> getRideBill(
            @Parameter(description = "Ride ID to bill", required = true)
            @PathVariable String rideId,
            @Parameter(description = "Fields to include in response")
            @RequestParam(required = false) String fields) {

        logger.info("Generating bill for ride: {}", rideId);
        try {
            return rideService.generateBill(rideId)
                    .map(billDetails -> {
                        Map<String, Object> response = Map.of(
                                "rideId", billDetails.getRideId(),
                                "driverId", billDetails.getDriverId(),
                                "totalFare", billDetails.getTotalFare(),
                                "currency", "USD",
                                "timestamp", System.currentTimeMillis(),
                                "_links", List.of(
                                        Map.of(
                                                "href", "/api/rides/" + rideId + "/bill",
                                                "rel", "self",
                                                "method", "GET"
                                        ),
                                        Map.of(
                                                "href", "/api/rides/" + rideId,
                                                "rel", "ride",
                                                "method", "GET"
                                        )
                                )
                        );
                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        Map<String, Object> errorResponse = Map.of(
                                "error", "Not Found",
                                "message", "Invalid or incomplete ride",
                                "rideId", rideId,
                                "timestamp", System.currentTimeMillis()
                        );
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                    });
        } catch (Exception e) {
            logger.error("Error generating bill: {}", e.getMessage());
            Map<String, Object> errorResponse = Map.of(
                    "error", "Internal Server Error",
                    "message", "Unable to generate bill",
                    "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
