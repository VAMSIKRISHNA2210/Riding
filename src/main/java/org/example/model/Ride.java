package org.example.model;

import lombok.Getter;
import javax.validation.constraints.*;

/**
 * Represents a ride in the ride-sharing system.
 * This class contains information about the ride, including its ID, driver, rider,
 * start and end locations, duration, and completion status.
 */
@Getter
public class Ride {

    @NotBlank(message = "Ride ID cannot be blank")
    @Size(min = 5, max = 50, message = "Ride ID must be between 5 and 50 characters")
    private final String id;

    @NotNull(message = "Driver cannot be null")
    private final Driver driver;

    @NotNull(message = "Rider cannot be null")
    private final Rider rider;

    private final double startLatitude;
    private final double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private double duration; // in minutes
    private boolean completed;

    /**
     * Constructs a new Ride with the given ID, driver, and rider.
     *
     * @param id     The unique identifier for the ride
     * @param driver The driver assigned to the ride
     * @param rider  The rider requesting the ride
     */
    public Ride(String id, Driver driver, Rider rider) {
        this.id = id;
        this.driver = driver;
        this.rider = rider;
        this.startLatitude = rider.getLatitude();
        this.startLongitude = rider.getLongitude();
        this.completed = false;
    }

    /**
     * Ends the ride by setting the end location, duration, and marking it as completed.
     *
     * @param endLatitude  The latitude of the end location
     * @param endLongitude The longitude of the end location
     * @param duration     The duration of the ride in minutes
     */
    public void endRide(double endLatitude, double endLongitude, double duration) {
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.duration = duration;
        this.completed = true;
    }
}
