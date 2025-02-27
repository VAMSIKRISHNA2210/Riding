package org.example.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Rider {
    // Getters
    @Getter
    private final String id;
    @Getter
    private final String name;
    @Getter
    private final double latitude;
    @Getter
    private final double longitude;
    private final Set<String> preferredDrivers;

    public Rider(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.preferredDrivers = new HashSet<>();
    }

    // Add a preferred driver for the rider
    public void addPreferredDriver(String driverId) {
        preferredDrivers.add(driverId);
    }

    // Returns a copy of preferred drivers to ensure immutability.
    public Set<String> getPreferredDrivers(){return new HashSet<>(preferredDrivers);}
}
