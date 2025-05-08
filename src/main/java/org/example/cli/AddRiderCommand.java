package org.example.cli;

import lombok.Setter;

public class AddRiderCommand implements Command {
    private final CommandHandler handler;
    @Setter
    private String[] args;

    public AddRiderCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.addRider(args);
    }
}
