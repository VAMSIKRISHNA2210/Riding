package org.example.cli;

import lombok.Setter;

public class StartRideCommand implements Command {
    private final CommandHandler handler;
    @Setter
    private String[] args;

    public StartRideCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.startRide(args);
    }
}
