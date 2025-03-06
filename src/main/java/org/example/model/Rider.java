package org.example.model;

import javax.validation.constraints.*;
import lombok.Getter;

/**
 * Represents a rider in the ride-sharing system.
 * This class contains information about the rider's ID and current location.
 */
@Getter
public class Rider {

    @NotBlank(message = "Driver ID cannot be blank")
    @Size(min = 3, max = 20, message = "Driver ID must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Driver ID must be alphanumeric")
    private final String id;

    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be at least -90.0")
    @DecimalMax(value = "90.0", message = "Latitude cannot be greater than 90.0")
    private double latitude;

    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be at least -180.0")
    @DecimalMax(value = "180.0", message = "Longitude cannot be greater than 180.0")
    private double longitude;

    /**
     * Constructs a new Rider with the given ID and location.
     *
     * @param id        The unique identifier for the rider
     * @param latitude  The initial latitude of the rider's location
     * @param longitude The initial longitude of the rider's location
     */
    public Rider(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Updates the rider's location.
     *
     * @param latitude  The new latitude of the rider's location
     * @param longitude The new longitude of the rider's location
     */
    public void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
