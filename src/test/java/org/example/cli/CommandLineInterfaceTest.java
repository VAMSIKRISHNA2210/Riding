package org.example.cli;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;
import org.example.service.BillService;
import org.example.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

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

        verify(rideService).addDriver("D1", "John", 10.0, 20.0);
        assert outputStream.toString().contains("Driver added successfully!");
    }

    @Test
    void testAddRiderCommand() {
        System.setIn(new ByteArrayInputStream("ADD_RIDER R1 Alice 15.5 25.5\nEXIT\n".getBytes()));

        cli.run();

        verify(rideService).addRider("R1", "Alice", 15.5, 25.5);
        assert outputStream.toString().contains("Rider added successfully!");
    }

    @Test
    void testStartRideCommand() {
        System.setIn(new ByteArrayInputStream("START_RIDE Ride1 R1 D1\nEXIT\n".getBytes()));

        cli.run();

        verify(rideService).startRide("Ride1", "R1", "D1");
        assert outputStream.toString().contains("Ride started successfully!");
    }

    @Test
    void testStopRideCommand() {
        System.setIn(new ByteArrayInputStream("STOP_RIDE Ride1 15.5 25.5 30\nEXIT\n".getBytes()));

        cli.run();

        verify(rideService).stopRide("Ride1", 15.5, 25.5, 30);
        assert outputStream.toString().contains("Ride stopped successfully!");
    }

    @Test
    void testGenerateBillCommand() {
        // Arrange
        String rideId = "Ride1";
        Rider rider = new Rider("R1", "Alice", 15.5, 25.5);
        Driver driver = new Driver("D1", "John", 10.0, 20.0);
        Ride ride = new Ride(rideId, rider, driver);
        ride.completeRide(16.0, 26.0, 30); // Mark as completed

        Map<String, Ride> ridesMap = new HashMap<>();
        ridesMap.put(rideId, ride);

        when(rideService.getRides()).thenReturn(ridesMap);

        System.setIn(new ByteArrayInputStream(("GENERATE_BILL " + rideId + "\nEXIT\n").getBytes()));

        // Act
        cli.run();

        // Assert
        verify(billService, times(1)).generateBill(ride);
        assert outputStream.toString().contains("Bill generated successfully!");
    }
}
