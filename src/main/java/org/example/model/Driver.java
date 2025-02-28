package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class Driver {

    @Setter
    @NotBlank(message = "Driver ID must not be blank")
    private String id;

    @Setter
    @NotBlank(message = "Driver name must not be blank")
    private String name;

    @Setter
    @NotNull(message = "Latitude must not be null")
    private Double latitude;

    @Setter
    @NotNull(message = "Longitude must not be null")
    private Double longitude;

    // Getters and setters
    @Setter
    private boolean available; // Tracks if the driver is available
    @Getter
    private double rating; // Average rating of the driver
    private int totalRatings; // Total number of ratings

    public Driver(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = true; // Drivers are available by default
        this.rating = 0.0; // Initial rating is 0
    }

    public synchronized void addRating(int rating) {
        this.rating = ((this.rating * totalRatings) + rating) / (totalRatings + 1);
        totalRatings++;
    }

}
