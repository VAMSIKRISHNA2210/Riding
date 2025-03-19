package org.example;

import org.example.service.BillDetails;
import org.example.service.RideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RideServiceTest {

    @Autowired
    private RideService rideService;

    @Test
    void testAddDriverAndMatchRider() {
        // Add driver and rider
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        // Match rider with nearby drivers
        List<String> matchedDrivers = rideService.matchRider("R1");

        assertEquals(1, matchedDrivers.size());
        assertTrue(matchedDrivers.contains("D1"));
    }

    @Test
    void testMatchRiderForMoreThan5km() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 35.0, 37.0);

        // Attempt to match rider (should return empty list)
        List<String> matchedDrivers = rideService.matchRider("R1");

        assertTrue(matchedDrivers.isEmpty());
    }

    @Test
    void testStartAndStopRide() {
        // Add driver and rider
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        // Start a ride with valid inputs
        String rideId = rideService.startRide("RIDE1", 1, "R1");
        assertEquals("RIDE1", rideId);

        // Stop the ride with valid inputs
        String stoppedRideId = rideService.stopRide("RIDE1", 11.0, 21.0, 15.0);
        assertEquals("RIDE1", stoppedRideId);
    }

    @Test
    void testStartRideInvalidDriverIndex() {
        // Add multiple drivers and a rider
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addDriver("D2", 11.0, 21.0);
        rideService.addRider("R1", 10.5, 20.5);

        // Attempt to start a ride with an invalid driver index (should throw exception)
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.startRide("RIDE2", 3, "R1") // Invalid index (3)
        );

        assertEquals("Invalid ride or already exists", exception.getMessage());
    }

    @Test
    void testStartRideDuplicateRideId() {
        // Add driver and rider
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        // Start a valid ride
        String firstResult = rideService.startRide("UNIQUE_RIDE_ID", 1, "R1");
        assertNotNull(firstResult);

        // Attempt to start another ride with the same ID (should throw exception)
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.startRide("UNIQUE_RIDE_ID", 1, "R1")
        );

        assertEquals("Invalid ride or already exists", exception.getMessage());
    }

    @Test
    void testStopRideValid() {
        // Add driver and rider
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        // Start a valid ride
        String startResult = rideService.startRide("VALID_RIDE_ID", 1, "R1");
        assertNotNull(startResult);

        // Stop the valid ride
        String stopResult = rideService.stopRide("VALID_RIDE_ID", 11.0, 21.0, 15);
        assertNotNull(stopResult);
        assertEquals("VALID_RIDE_ID", stopResult);
    }

    @Test
    void testStopRideInvalidRideId() {
        // Attempt to stop a non-existent or invalid ride (should throw exception)
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.stopRide("INVALID_RIDE_ID", 11.0, 21.0, 15)
        );

        assertEquals("Invalid or already completed ride", exception.getMessage());
    }

    @Test
    void testStopRideAlreadyCompleted() {
        // Add driver and rider
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        // Start a valid ride
        String startResult = rideService.startRide("COMPLETED_RIDE_ID", 1, "R1");
        assertNotNull(startResult);

        // Stop the valid ride for the first time
        String firstStopResult = rideService.stopRide("COMPLETED_RIDE_ID", 11.0, 21.0, 15);
        assertNotNull(firstStopResult);

        // Attempt to stop the completed ride again (should throw exception)
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.stopRide("COMPLETED_RIDE_ID", 12.0, 22.0, 30)
        );

        assertEquals("Invalid or already completed ride", exception.getMessage());
    }

    @Test
    void testGenerateBillValid() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        String startResult = rideService.startRide("ride123", 1, "R1");
        assertNotNull(startResult);

        String stopResult = rideService.stopRide("ride123", 11.0, 21.0, 15);
        assertNotNull(stopResult);

        Optional<BillDetails> billDetailsOptional = rideService.generateBill("ride123");

        assertTrue(billDetailsOptional.isPresent());

        BillDetails billDetails = billDetailsOptional.get();
        assertEquals(new BigDecimal("101.52"), billDetails.getTotalFare());
    }
}
