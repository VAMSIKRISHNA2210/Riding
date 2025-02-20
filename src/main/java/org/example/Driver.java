package org.example;

public class Driver {
    private final String id;
    private final double latitude;
    private final double longitude;
    private boolean available;
    private double rating;
    private int totalRatings;

    public Driver(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.available = true;
        this.rating = 0;
        this.totalRatings = 0;
    }

    public void addRating(int rating) {
        this.rating = ((this.rating * totalRatings) + rating) / (totalRatings + 1);
        this.totalRatings++;
    }

    public String getId() { return id; }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public double getRating() { return rating; }
}