package org.example.cli;

public class BillCommand implements Command {
    private final CommandHandler handler;
    private final String rideId;

    public BillCommand(CommandHandler handler, String rideId) {
        this.handler = handler;
        this.rideId = rideId;

        if (rideId == null || rideId.isEmpty()) {
            throw new IllegalArgumentException("Ride ID cannot be empty");
        }
    }

    @Override
    public void execute() {
        handler.generateBill(rideId);
    }
}
