package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RestApiApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MainTest {

    @Autowired
    private ApplicationContext context;

    private ByteArrayInputStream inContent;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    private String getFilteredOutput() {
        return outContent.toString()
                .lines()
                .filter(line -> !line.contains("INFO") && !line.contains("WARN") && !line.contains("ERROR"))
                .collect(Collectors.joining("\n"));
    }
    @Test
    void testGenerateBill() {
        String input = """
                ADD_DRIVER D1 10.0 20.0
                ADD_RIDER R1 12.0 22.0
                MATCH R1
                START_RIDE RIDE1 1 R1
                STOP_RIDE RIDE1 15.0 24.0 15.0
                BILL RIDE1
                QUIT
                """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
                Driver added successfully.
                Rider added successfully.
                Matched drivers: D1
                Ride started successfully.
                Ride stopped successfully.
                Total Bill for Ride ID RIDE1 with Driver ID D1 is 124.12
                Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testAddDriverInvalidParameters() {
        String input = """
            ADD_DRIVER D1
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Invalid parameters entered. Usage: ADD_DRIVER <driverId> <latitude> <longitude>
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testAddDriverInvalidCoordinates() {
        String input = """
            ADD_DRIVER D1 -100.0 20.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Error: Invalid latitude or longitude values.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testAddDriverNonNumericCoordinates() {
        String input = """
            ADD_DRIVER D1 abc 20.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Error: Latitude and longitude must be valid numbers.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testAddRiderInvalidParameters() {
        String input = """
            ADD_RIDER R1
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Invalid parameters entered. Usage: ADD_RIDER <riderId> <latitude> <longitude>
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testAddRiderInvalidCoordinates() {
        String input = """
            ADD_RIDER R1 10.0 200.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Error: Invalid latitude or longitude values.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testAddRiderNonNumericCoordinates() {
        String input = """
            ADD_RIDER R1 10.0 xyz
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Error: Latitude and longitude must be valid numbers.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testNoAvailableDrivers() {
        String input = """
            ADD_RIDER R1 12.0 22.0
            MATCH R1
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Rider added successfully.
            No drivers matched for Rider ID: R1
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testStopRideInvalidParameters() {
        String input = """
            STOP_RIDE RIDE1
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Invalid parameters entered. Usage: STOP_RIDE <rideId> <endLatitude> <endLongitude> <duration>
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testStopRideInvalidCoordinates() {
        String input = """
            ADD_DRIVER D1 10.0 20.0
            ADD_RIDER R1 12.0 22.0
            MATCH R1
            START_RIDE RIDE1 1 R1
            STOP_RIDE RIDE1 -100.0 24.0 15.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Driver added successfully.
            Rider added successfully.
            Matched drivers: D1
            Ride started successfully.
            Error: Invalid latitude or longitude values.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testStopRideInvalidDuration() {
        String input = """
            ADD_DRIVER D1 10.0 20.0
            ADD_RIDER R1 12.0 22.0
            MATCH R1
            START_RIDE RIDE1 1 R1
            STOP_RIDE RIDE1 15.0 24.0 -5.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Driver added successfully.
            Rider added successfully.
            Matched drivers: D1
            Ride started successfully.
            Error: Duration must be greater than 0.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testStopRideNonNumericValues() {
        String input = """
            ADD_DRIVER D1 10.0 20.0
            ADD_RIDER R1 12.0 22.0
            MATCH R1
            START_RIDE RIDE1 1 R1
            STOP_RIDE RIDE1 15.0 abc 15.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Driver added successfully.
            Rider added successfully.
            Matched drivers: D1
            Ride started successfully.
            Error: Latitude, longitude, and duration must be valid numbers.
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testStopRideInvalidRide() {
        String input = """
            STOP_RIDE RIDE1 15.0 24.0 15.0
            QUIT
            """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
            Error stopping ride: Invalid or already completed ride
            Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }

    @Test
    void testRideStartFailsInvalidCommand() {
        String input = """
                ADD_RIDER R1 12.0 22.0
                START_RIDE RIDE1 1 R1
                QUIT
                """;
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        RestApiApplication.runCliMode(context);

        String expectedOutput = """
                Rider added successfully.
                Error starting ride: Invalid ride or already exists
                Exiting the application.""";

        assertEquals(expectedOutput.trim(), getFilteredOutput().trim());
    }


}
