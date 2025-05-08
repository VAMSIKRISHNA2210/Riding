package org.example.cli;

import lombok.Setter;

public class StopRideCommand implements Command {
    private final CommandHandler handler;
    @Setter
    private String[] args;

    public StopRideCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.stopRide(args);
    }
}
