package org.example;

public class Ride {
    private final Rider rider;
    private final Driver driver;
    private boolean completed;

    public Ride(Rider rider, Driver driver) {
        this.rider = rider;
        this.driver = driver;
        this.completed = false;
    }

    public void completeRide() {
        this.completed = true;
        driver.setAvailable(true);
    }

    public Rider getRider() { return rider; }
    public Driver getDriver() { return driver; }
    public boolean isCompleted() { return completed; }
}