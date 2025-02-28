package org.example.controller;


import org.example.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/riders")
@Validated
public class RiderController {

    private final RideService rideService;

    public RiderController(RideService rideService) {
        this.rideService = rideService;
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> addRider(@RequestParam String id,
                                            @RequestParam String name,
                                            @RequestParam double latitude,
                                            @RequestParam double longitude) {
        rideService.addRider(id, name, latitude, longitude);
        return ResponseEntity.ok("Rider added successfully");
    }
}

