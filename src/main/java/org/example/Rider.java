package org.example;

public class Rider {
    private final double latitude;
    private final double longitude;

    public Rider(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
