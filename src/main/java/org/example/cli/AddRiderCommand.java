package org.example.cli;

public class AddRiderCommand implements Command {
    private final CommandHandler handler;
    private final String riderId;
    private final double latitude;
    private final double longitude;

    public AddRiderCommand(CommandHandler handler, String riderId, double latitude, double longitude) {
        this.handler = handler;
        this.riderId = riderId;
        this.latitude = latitude;
        this.longitude = longitude;

        if (riderId == null || riderId.isEmpty()) {
            throw new IllegalArgumentException("Rider ID cannot be empty");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
    }

    @Override
    public void execute() {
        handler.addRider(riderId, latitude, longitude);
    }
}
