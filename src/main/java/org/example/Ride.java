package org.example;

public class Ride {
    private final Rider rider;
    private final Driver driver;

    private int duration; // in minutes
    private boolean completed;

    public Ride(String rideId, Rider rider, Driver driver) {
        this.rider = rider;
        this.driver = driver;

        double startLatitude = rider.getLatitude();
        double startLongitude = rider.getLongitude();
        this.completed = false; // Initially not completed
    }

    public void completeRide(double endLatitude, double endLongitude, int duration) {
        this.duration = duration;
        this.completed = true; // Mark as completed
        driver.setAvailable(true); // Make the driver available again
    }

    public boolean isCompleted() {
        return completed;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public int getDuration() {
        return duration;
    }
}
