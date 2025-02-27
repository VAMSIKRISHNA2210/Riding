package org.example.cli;

import org.example.service.BillService;
import org.example.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CommandLineInterfaceTest {

    @Mock
    private RideService rideService;

    @Mock
    private BillService billService;

    private CommandLineInterface cli;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStream)); // Redirect output to capture it
        cli = new CommandLineInterface(rideService, billService);
    }

    @Test
    void testAddDriverCommand() {
        System.setIn(new ByteArrayInputStream("ADD_DRIVER D1 John 10.0 20.0\nEXIT\n".getBytes()));

        cli.run();

        verify(rideService, times(1)).addDriver("D1", "John", 10.0, 20.0);
        assertTrue(outputStream.toString().contains("Driver added successfully"));
    }

    @Test
    void testAddRiderCommand() {
        System.setIn(new ByteArrayInputStream("ADD_RIDER R1 Alice 15.0 25.0\nEXIT\n".getBytes()));

        cli.run();

        verify(rideService, times(1)).addRider("R1", "Alice", 15.0, 25.0);
        assertTrue(outputStream.toString().contains("Rider added successfully"));
    }

    @Test
    void testStartRideCommand() {
        System.setIn(new ByteArrayInputStream("START_RIDE Ride1 R1 D1\nEXIT\n".getBytes()));

        cli.run();

        verify(rideService, times(1)).startRide("Ride1", "R1", "D1");
        assertTrue(outputStream.toString().contains("Ride started successfully"));
    }
}
