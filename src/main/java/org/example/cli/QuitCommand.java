package org.example.cli;

import org.example.RiderCli;

public class QuitCommand implements Command {
    private final RiderCli riderCli;

    public QuitCommand(RiderCli riderCli) {
        this.riderCli = riderCli;
    }

    @Override
    public void execute() {
        System.out.println("Exiting the application.");
        riderCli.stop();
    }
}
