package org.example.controller;

import org.example.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final RideService rideService;

    public DriverController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDriver(@RequestParam String id,
                                            @RequestParam String name,
                                            @RequestParam double latitude,
                                            @RequestParam double longitude) {
        rideService.addDriver(id, name, latitude, longitude);
        return ResponseEntity.ok("Driver added successfully");
    }
}
