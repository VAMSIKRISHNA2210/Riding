package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RiderApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CliTest {

    @Autowired
    private ApplicationContext context;

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

    record TestData(String name, String input, String expected) {}

    static Stream<TestData> cliTestCases() {
        return Stream.of(
                new TestData("GenerateBill",
                """
                ADD_DRIVER D1 10.0 20.0
                ADD_RIDER R1 12.0 22.0
                MATCH R1
                START_RIDE RIDE1 1 R1
                STOP_RIDE RIDE1 15.0 24.0 15.0
                BILL RIDE1
                QUIT
                """,
                """
                Driver added successfully.
                Rider added successfully.
                Matched drivers: D1
                Ride started successfully.
                Ride stopped successfully.
                Total Bill for Ride ID RIDE1 with Driver ID D1 is 124.12
                Exiting the application.
                """),

                new TestData("AddDriverInvalidParameters",
                """
                ADD_DRIVER D1
                QUIT
                """,
                """
                Invalid parameters.Usage: ADD_DRIVER <id> <latitude> <longitude>
                Exiting the application.
                """),

                new TestData("AddDriverInvalidCoordinates",
                """
                ADD_DRIVER D1 -100.0 20.0
                QUIT
                """,
                """
                Invalid parameters.Latitude must be between -90 and 90
                Exiting the application.
                """),

                new TestData("AddDriverNonNumericCoordinates",
                """
                ADD_DRIVER D1 abc 20.0
                QUIT
                """,
                """
                Invalid parameters.For input string: "abc"
                Exiting the application.
                """),

                new TestData("AddRiderInvalidParameters",
                """
                ADD_RIDER R1
                QUIT
                """,
                """
                Invalid parameters.Usage: ADD_RIDER <id> <latitude> <longitude>
                Exiting the application.
                """),

                new TestData("AddRiderInvalidCoordinates",
                """
                ADD_RIDER R1 10.0 200.0
                QUIT
                """,
                """
                Invalid parameters.Longitude must be between -180 and 180
                Exiting the application.
                """),

                new TestData("AddRiderNonNumericCoordinates",
                """
                ADD_RIDER R1 10.0 xyz
                QUIT
                """,
                """
                Invalid parameters.For input string: "xyz"
                Exiting the application.
                """),

                new TestData("NoAvailableDrivers",
                """
                ADD_RIDER R1 12.0 22.0
                MATCH R1
                QUIT
                """,
                """
                Rider added successfully.
                No drivers matched for Rider ID: R1
                Exiting the application.
                """),

                new TestData("StopRideInvalidParameters",
                """
                STOP_RIDE RIDE1
                QUIT
                """,
                """
                Invalid parameters.Usage: STOP_RIDE <ride_id> <end_latitude> <end_longitude> <duration>
                Exiting the application.
                """),

                new TestData("StopRideInvalidCoordinates",
                """
                ADD_DRIVER D1 10.0 20.0
                ADD_RIDER R1 12.0 22.0
                MATCH R1
                START_RIDE RIDE1 1 R1
                STOP_RIDE RIDE1 -100.0 24.0 15.0
                QUIT
                """,
                """
                Driver added successfully.
                Rider added successfully.
                Matched drivers: D1
                Ride started successfully.
                Invalid parameters.End latitude must be between -90 and 90
                Exiting the application.
                """),

                new TestData("StopRideInvalidDuration",
                """
                ADD_DRIVER D1 10.0 20.0
                ADD_RIDER R1 12.0 22.0
                MATCH R1
                START_RIDE RIDE1 1 R1
                STOP_RIDE RIDE1 15.0 24.0 -5.0
                QUIT
                """,
                """
                Driver added successfully.
                Rider added successfully.
                Matched drivers: D1
                Ride started successfully.
                Invalid parameters.Duration must be greater than 0
                Exiting the application.
                """),

                new TestData("StopRideNonNumericValues",
                """
                ADD_DRIVER D1 10.0 20.0
                ADD_RIDER R1 12.0 22.0
                MATCH R1
                START_RIDE RIDE1 1 R1
                STOP_RIDE RIDE1 15.0 abc 15.0
                QUIT
                """,
                """
                Driver added successfully.
                Rider added successfully.
                Matched drivers: D1
                Ride started successfully.
                Invalid parameters.For input string: "abc"
                Exiting the application.
                """),

                new TestData("StopRideInvalidRide",
                """
                STOP_RIDE RIDE1 15.0 24.0 15.0
                QUIT
                """,
                """
                Error stopping ride: Invalid or already completed ride
                Exiting the application.
                """),

                new TestData("RideStartFailsInvalidCommand",
                """
                ADD_RIDER R1 12.0 22.0
                START_RIDE RIDE1 1 R1
                QUIT
                """,
                """
                Rider added successfully.
                Error starting ride: Invalid ride or already exists
                Exiting the application.
                """)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cliTestCases")
    void testCliCommands(TestData testCase) {
        System.setIn(new ByteArrayInputStream(testCase.input().getBytes()));
        RiderApplication.runCliMode(context);
        assertEquals(testCase.expected().trim(), getFilteredOutput().trim());
    }
}
