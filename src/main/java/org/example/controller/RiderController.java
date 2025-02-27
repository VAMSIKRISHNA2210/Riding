package org.example.controller;

import org.example.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riders")
public class RiderController {

    private final RideService rideService;

    public RiderController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRider(@RequestParam String id,
                                           @RequestParam String name,
                                           @RequestParam double latitude,
                                           @RequestParam double longitude) {
        rideService.addRider(id, name, latitude, longitude);
        return ResponseEntity.ok("Rider added successfully");
    }
}
