package org.example.cli;

public class StopRideCommand implements Command {
    private final CommandHandler handler;
    private final String rideId;
    private final double endLatitude;
    private final double endLongitude;
    private final double duration;

    public StopRideCommand(CommandHandler handler, String rideId, double endLatitude, double endLongitude, double duration) {
        this.handler = handler;
        this.rideId = rideId;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.duration = duration;

        if (rideId == null || rideId.isEmpty()) {
            throw new IllegalArgumentException("Ride ID cannot be empty");
        }
        if (endLatitude < -90 || endLatitude > 90) {
            throw new IllegalArgumentException("End latitude must be between -90 and 90");
        }
        if (endLongitude < -180 || endLongitude > 180) {
            throw new IllegalArgumentException("End longitude must be between -180 and 180");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
    }

    @Override
    public void execute() {
        handler.stopRide(rideId, endLatitude, endLongitude, duration);
    }
}
