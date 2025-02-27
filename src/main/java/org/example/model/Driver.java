package org.example.model;

import lombok.Getter;
import lombok.Setter;

public class Driver {
    // Getters and setters
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final double latitude;
    @Getter
    private final double longitude;
    @Setter
    @Getter
    private boolean available;
    @Getter
    private double rating;
    private int totalRatings;

    public Driver(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = true; // Drivers are available by default
        this.rating = 0.0; // Initial rating is 0
        this.totalRatings = 0; // No ratings initially
    }

    // Add a rating to the driver
    public synchronized void addRating(int rating) {
        this.rating = ((this.rating * totalRatings) + rating) / (totalRatings + 1);
        totalRatings++;
    }

}