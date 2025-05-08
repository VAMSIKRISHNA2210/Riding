package org.example.cli;

public interface Command {
    void execute();
    default void setArgs(String[] args) {} // Optional
}
