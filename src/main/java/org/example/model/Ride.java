package org.example.model;

import lombok.Getter;

@Getter
public class Ride {
    // Getters
    private final String rideId;
    private final Rider rider;
    private final Driver driver;
    private final double startLatitude;
    private final double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private int duration; // in minutes
    private boolean completed;

    public Ride(String rideId, Rider rider, Driver driver) {
        this.rideId = rideId;
        this.rider = rider;
        this.driver = driver;
        this.startLatitude = rider.getLatitude();
        this.startLongitude = rider.getLongitude();
        this.completed = false; // Initially not completed
    }

    /**
     * Marks the ride as completed and sets the ending location and duration.
     *
     * @param endLatitude  The latitude of the ending location.
     * @param endLongitude The longitude of the ending location.
     * @param duration     The duration of the ride in minutes.
     */
    public void completeRide(double endLatitude, double endLongitude, int duration) {
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.duration = duration;
        this.completed = true; // Mark as completed
        driver.setAvailable(true); // Make the driver available again
    }

    /**
     * Calculates the distance traveled during the ride using Euclidean distance.
     *
     * @return The distance traveled in kilometers.
     */
    public double calculateDistance() {
        return Math.sqrt(Math.pow(endLatitude - startLatitude, 2) + Math.pow(endLongitude - startLongitude, 2));
    }

}
