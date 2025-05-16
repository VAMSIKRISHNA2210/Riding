package org.example.cli;

public class AddDriverCommand implements Command {
    private final CommandHandler handler;
    private final String driverId;
    private final double latitude;
    private final double longitude;

    public AddDriverCommand(CommandHandler handler, String driverId, double latitude, double longitude) {
        this.handler = handler;
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;

        if (driverId == null || driverId.isEmpty()) {
            throw new IllegalArgumentException("Driver ID cannot be empty");
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
        handler.addDriver(driverId, latitude, longitude);
    }
}
