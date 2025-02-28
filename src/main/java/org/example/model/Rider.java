package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class Rider {

    // Getters and setters
    @NotBlank(message = "Rider ID must not be blank")
    private String id;

    @NotBlank(message = "Rider name must not be blank")
    private String name;

    @NotNull(message = "Latitude must not be null")
    private Double latitude;

    @NotNull(message = "Longitude must not be null")
    private Double longitude;

    // Stores IDs of preferred drivers

    public Rider(String id, String name, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    private final Set<String> preferredDrivers = new HashSet<>();

    public void addPreferredDriver(String driverId) {
        preferredDrivers.add(driverId);
    }

    public Set<String> getPreferredDrivers() {
        return new HashSet<>(preferredDrivers); // Return a copy for immutability
    }


}
