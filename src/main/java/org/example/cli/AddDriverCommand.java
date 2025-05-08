package org.example.cli;

import lombok.Setter;

public class AddDriverCommand implements Command {
    private final CommandHandler handler;
    @Setter
    private String[] args;

    public AddDriverCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.addDriver(args);
    }
}
