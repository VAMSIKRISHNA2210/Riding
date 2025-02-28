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

    @RequestMapping(value = "/start", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> startRide(@RequestParam String rideId,
                                            @RequestParam String riderId,
                                            @RequestParam String driverId) {
        rideService.startRide(rideId, riderId, driverId);
        return ResponseEntity.ok("Ride started successfully");
    }

    @RequestMapping(value = "/stop", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> stopRide(@RequestParam String rideId,
                                           @RequestParam double endLatitude,
                                           @RequestParam double endLongitude,
                                           @RequestParam int duration) {
        rideService.stopRide(rideId, endLatitude, endLongitude, duration);
        return ResponseEntity.ok("Ride stopped successfully");
    }

    @GetMapping("/bill")
    public ResponseEntity<String> generateBill(@RequestParam String rideId) {
        Ride ride = rideService.getRides().get(rideId);
        if (ride == null || !ride.isCompleted()) {
            return ResponseEntity.badRequest().body("Invalid or incomplete ride");
        }
        billService.generateBill(ride);
        return ResponseEntity.ok("Bill generated successfully");
    }
}
