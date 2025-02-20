package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    // Test Case 1: Add a driver and verify the output
    @Test
    void testAddDriver() {
        String input = "ADD_DRIVER D1 John 10.0 20.0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("DRIVER_ADDED"));
    }

    // Test Case 2: Add a rider and verify the output
    @Test
    void testAddRider() {
        String input = "ADD_RIDER R1 Alice 15.0 25.0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("RIDER_ADDED"));
    }

    // Test Case 3: Match drivers when no drivers are available
    @Test
    void testMatchNoDrivers() {
        String input = "ADD_RIDER R1 Alice 15.0 25.0\nMATCH R1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("NO_DRIVERS_AVAILABLE"));
    }

    // Test Case 4: Match drivers when drivers are available
    @Test
    void testMatchDrivers() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_DRIVER D2 Mike 11.0 21.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "MATCH R1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("DRIVERS_MATCHED D1 D2"));
    }

    // Test Case 5: Start a ride successfully
    @Test
    void testStartRide() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "START_RIDE Ride1 R1 D1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("RIDE_STARTED Ride1"));
    }

    // Test Case 6: Stop a ride and generate a bill
    @Test
    void testStopRideAndGenerateBill() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "START_RIDE Ride1 R1 D1\n" +
                        "STOP_RIDE Ride1 15.0 25.0 30\n" +
                        "BILL Ride1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("BILL:"));
    }

    // Test Case 7: Rate a driver after completing a ride
    @Test
    void testRateDriver() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "START_RIDE Ride1 R1 D1\n" +
                        "STOP_RIDE Ride1 15.0 25.0 30\n" +
                        "RATE_DRIVER Ride1 5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("DRIVER_RATED"));
    }

    // Test Case 8: Add a preferred driver and match them first
    @Test
    void testPreferredDriver() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_DRIVER D2 Mike 10.2 20.2\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "ADD_PREFERRED_DRIVER R1 D2\n" +
                        "MATCH R1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("DRIVERS_MATCHED D2"));
    }

    // Test Case 9: Attempt to start a ride with an unavailable driver
    @Test
    void testStartRideWithUnavailableDriver() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "START_RIDE Ride1 R1 D1\n" +
                        "START_RIDE Ride2 R2 D1\n"; // Driver is already on a ride

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("RIDE_STARTED Ride1"));
        assertTrue(output.contains("INVALID_RIDE_REQUEST")); // Second ride should fail
    }

    // Test Case 10: Attempt to generate a bill for an incomplete ride
    @Test
    void testGenerateBillForIncompleteRide() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "START_RIDE Ride1 R1 D1\n" +
                        "BILL Ride1\n"; // Attempt to generate bill without stopping the ride

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID_RIDE_FOR_BILLING")); // Bill generation should fail
    }

    @Test
    void testDistanceBasedDriverMatching() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_DRIVER D2 Mike 11.0 21.0\n" +
                        "ADD_DRIVER D3 Sarah 50.0 50.0\n" + // Out of range (>5km)
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "MATCH R1\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();


        String output = outputStream.toString().trim();
        assertTrue(output.contains("DRIVERS_MATCHED D1 D2")); // Only drivers within range should match
    }

    @Test
    void testRateDriverAfterRide() {
        String input =
                "ADD_DRIVER D1 John 10.0 20.0\n" +
                        "ADD_RIDER R1 Alice 10.5 20.5\n" +
                        "START_RIDE Ride1 R1 D1\n" +
                        "STOP_RIDE Ride1 15.0 25.0 30\n" +
                        "RATE_DRIVER Ride1 4\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main main = new Main();
        main.run();

        String output = outputStream.toString().trim();
        assertTrue(output.contains("DRIVER_RATED"));
    }

}
