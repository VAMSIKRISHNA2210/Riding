package org.example.controller;

import org.example.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@Validated // Enables validation for method parameters
public class DriverController {

    private final RideService rideService;

    public DriverController(RideService rideService) {
        this.rideService = rideService;
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> addDriver(@RequestParam String id,
                                            @RequestParam String name,
                                            @RequestParam double latitude,
                                            @RequestParam double longitude) {
        rideService.addDriver(id, name, latitude, longitude);
        return ResponseEntity.ok("Driver added successfully");
    }

}
