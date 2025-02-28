package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class Ride {

    // Getters and setters
    @Setter
    @NotBlank(message = "Ride ID must not be blank")
    private String rideId;

    @Setter
    @NotNull(message = "Rider must not be null")
    private Rider rider;

    @Setter
    @NotNull(message = "Driver must not be null")
    private Driver driver;

    private boolean completed;
    // Getter for duration
    private int duration; // Duration of the ride in minutes
    @Getter
    private double endLatitude;
    @Getter
    private double endLongitude;


    public Ride(String rideId, Rider rider, Driver driver) {
        this.rideId = rideId;
        this.rider = rider;
        this.driver = driver;
        this.completed = false;
    }

    // Mark the ride as completed
    public void completeRide(double endLatitude,double endLongitude,int duration) {
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.completed = true;
        this.duration = duration; // Set the duration when completing the ride
        driver.setAvailable(true); // Make the driver available again
    }

}
