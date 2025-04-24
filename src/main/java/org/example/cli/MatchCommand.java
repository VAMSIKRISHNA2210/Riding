package org.example.cli;

import lombok.Setter;

public class MatchCommand implements Command {
    private final CommandHandler handler;
    @Setter
    private String[] args;

    public MatchCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.matchRider(args);
    }
}
