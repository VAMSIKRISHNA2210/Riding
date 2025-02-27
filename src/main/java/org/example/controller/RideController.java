package org.example.controller;

import org.example.model.Ride;
import org.example.service.BillService;
import org.example.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rides")
public class RideController {

    private final RideService rideService;
    private final BillService billService;

    public RideController(RideService rideService, BillService billService) {
        this.rideService = rideService;
        this.billService = billService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startRide(@RequestParam String rideId,
                                            @RequestParam String riderId,
                                            @RequestParam String driverId) {
        try {
            rideService.startRide(rideId, riderId, driverId);
            return ResponseEntity.ok("Ride started successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopRide(@RequestParam String rideId,
                                           @RequestParam double endLatitude,
                                           @RequestParam double endLongitude,
                                           @RequestParam int duration) {
        try {
            // Pass all required arguments to stopRide
            rideService.stopRide(rideId, endLatitude, endLongitude, duration);
            return ResponseEntity.ok("Ride stopped successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/bill")
    public ResponseEntity<String> generateBill(@RequestParam String rideId) {
        Ride ride = rideService.getRides().get(rideId);
        if (ride == null || !ride.isCompleted()) {
            return ResponseEntity.badRequest().body("Invalid ride for billing");
        }
        try {
            billService.generateBill(ride);
            return ResponseEntity.ok("Bill generated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
