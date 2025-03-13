package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inContent; // Declare here


    @BeforeEach
    void setUpStreams() {
        inContent = new ByteArrayInputStream("".getBytes());  // Initialize here
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(System.in);
    }

    @Test
    void testMainMethodWithValidInput() {
        // Prepare input for the CLI commands
        String input = "ADD_DRIVER D1 0 0\nADD_RIDER R1 1 1\nMATCH R1\nSTART_RIDE RIDE1 1 R1\nSTOP_RIDE RIDE1 4 4 10\nBILL RIDE1\nEXIT\n";
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        // Set up a ListAppender to capture logs
        Logger logger = (Logger) LoggerFactory.getLogger("org.example.cli.CommandHandler");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Run the main method
        Main.main(new String[]{});

        // Verify captured logs
        String expectedLogOutput =
                "Driver added successfully. Driver ID: D1\n" +
                        "Rider added successfully. Rider ID: R1\n" +
                        "Matched drivers for Rider ID R1: D1\n" +
                        "Ride started successfully. Ride ID: RIDE1\n" +
                        "Ride stopped successfully. Ride ID: RIDE1\n" +
                        "Total Bill for Ride ID RIDE1 with Driver ID D1 is 87.31";

        StringBuilder actualLogOutput = new StringBuilder();
        for (ILoggingEvent event : listAppender.list) {
            actualLogOutput.append(event.getFormattedMessage()).append("\n");
        }

        assertEquals(expectedLogOutput.trim(), actualLogOutput.toString().trim());
    }

    @Test
    void testMainMethodWithInvalidCommand() {
        String input = "INVALID_COMMAND\nEXIT\n";
        inContent = new ByteArrayInputStream(input.getBytes());  // Re-initialize for each test
        System.setIn(inContent);

        Main.main(new String[]{});

        assertTrue(outContent.toString().contains("INVALID_COMMAND"));
    }
}
