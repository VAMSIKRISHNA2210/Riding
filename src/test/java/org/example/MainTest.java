package org.example;

import org.example.service.RideService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inContent; // Declare here
    private final PrintStream originalIn = System.out;

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
        String input = "ADD_DRIVER D1 0 0\nADD_RIDER R1 1 1\nMATCH R1\nSTART_RIDE RIDE1 1 R1\nSTOP_RIDE RIDE1 4 4 10\nBILL RIDE1\nEXIT\n";
        inContent = new ByteArrayInputStream(input.getBytes());  // Re-initialize for each test
        System.setIn(inContent);

        Main.main(new String[]{});

        String expectedOutput = "Driver added successfully\nRider added successfully\nD1\nRIDE_STARTED RIDE1\nRIDE_STOPPED RIDE1\nBILL RIDE1 D1 87.31\n";
        assertEquals(expectedOutput.trim(), outContent.toString().trim());
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
