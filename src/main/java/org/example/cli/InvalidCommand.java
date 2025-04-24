package org.example.cli;

public class InvalidCommand implements Command {
    @Override
    public void execute() {
        System.out.println("INVALID_COMMAND");
    }
}
