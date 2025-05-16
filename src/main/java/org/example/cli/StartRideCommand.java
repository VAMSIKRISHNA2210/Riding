package org.example.cli;

public class StartRideCommand implements Command {
    private final CommandHandler handler;
    private final String rideId;
    private final int driverIndex;
    private final String riderId;

    public StartRideCommand(CommandHandler handler, String rideId, int driverIndex, String riderId) {
        this.handler = handler;
        this.rideId = rideId;
        this.driverIndex = driverIndex;
        this.riderId = riderId;

        if (rideId == null || rideId.isEmpty()) {
            throw new IllegalArgumentException("Ride ID cannot be empty");
        }
        if (riderId == null || riderId.isEmpty()) {
            throw new IllegalArgumentException("Rider ID cannot be empty");
        }
        if (driverIndex <= 0) {
            throw new IllegalArgumentException("Driver index must be greater than 0");
        }
    }

    @Override
    public void execute() {
        handler.startRide(rideId, driverIndex, riderId);
    }
}
