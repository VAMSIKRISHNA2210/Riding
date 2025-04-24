package org.example.cli;

import lombok.Setter;

public class BillCommand implements Command {
    private final CommandHandler handler;
    @Setter
    private String[] args;

    public BillCommand(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() {
        handler.generateBill(args);
    }
}
