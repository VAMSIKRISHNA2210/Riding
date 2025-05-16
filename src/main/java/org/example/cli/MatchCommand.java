package org.example.cli;

public class MatchCommand implements Command {
    private final CommandHandler handler;
    private final String riderId;

    public MatchCommand(CommandHandler handler, String riderId) {
        this.handler = handler;
        this.riderId = riderId;

        if (riderId == null || riderId.isEmpty()) {
            throw new IllegalArgumentException("Rider ID cannot be empty");
        }
    }

    @Override
    public void execute() {
        handler.matchRider(riderId);
    }
}
