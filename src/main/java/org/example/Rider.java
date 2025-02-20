package org.example;

import java.util.HashSet;
import java.util.Set;

public class Rider {
    private final double latitude;
    private final double longitude;
    private final Set<String> preferredDrivers;

    public Rider(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.preferredDrivers = new HashSet<>();
    }

    public void addPreferredDriver(String driverId) {
        preferredDrivers.add(driverId);
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Set<String> getPreferredDrivers() { return preferredDrivers; }
}