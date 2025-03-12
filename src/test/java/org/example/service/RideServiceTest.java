package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RideServiceTest {
    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideService = new RideService();
    }

    @Test
    void testAddDriverAndMatchRider() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        List<String> matches = rideService.matchRider("R1");

        assertEquals(1, matches.size());
        assertTrue(matches.contains("D1"));
    }

    @Test
    void testMatchRiderMultipleDrivers() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addDriver("D2", 11.0, 21.0);
        rideService.addDriver("D3", 15.0, 25.0);
        rideService.addRider("R1", 10.5, 20.5);

        List<String> matches = rideService.matchRider("R1");

        assertEquals(3, matches.size());
        assertTrue(matches.contains("D1"));
        assertTrue(matches.contains("D2"));
        assertTrue(matches.contains("D3"));
    }

    @Test
    void testMatchRiderInvalidRider() {
        List<String> matches = rideService.matchRider("invalidRider");
        assertTrue(matches.isEmpty());
    }

    @Test
    void testStartRideValid() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        String result = rideService.startRide("ride123", 1, "R1");

        assertNotNull(result);
        assertEquals("ride123", result);
    }

    @Test
    void testStartRideInvalidDriverNumber() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addDriver("D2", 11.0, 21.0);
        rideService.addRider("R1", 10.5, 20.5);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.startRide("ride123", 3, "R1")
        );

        assertEquals("Invalid ride or already exists", exception.getMessage());
    }

    @Test
    void testStartRideDuplicateRideId() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        String firstResult = rideService.startRide("ride123", 1, "R1");
        assertNotNull(firstResult);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.startRide("ride123", 1, "R1")
        );

        assertEquals("Invalid ride or already exists", exception.getMessage());
    }

    @Test
    void testStopRideValid() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        String startResult = rideService.startRide("ride123", 1, "R1");
        assertNotNull(startResult);

        String stopResult = rideService.stopRide("ride123", 11.0, 21.0, 15);
        assertNotNull(stopResult);
        assertEquals("ride123", stopResult);
    }

    @Test
    void testStopRideInvalidRide() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.stopRide("invalidRide", 11.0, 21.0, 15)
        );

        assertEquals("Invalid or already completed ride", exception.getMessage());
    }

    @Test
    void testStopRideAlreadyCompleted() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        String startResult = rideService.startRide("ride123", 1, "R1");
        assertNotNull(startResult);

        String firstStopResult = rideService.stopRide("ride123", 11.0, 21.0, 15);
        assertNotNull(firstStopResult);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> rideService.stopRide("ride123", 12.0, 22.0, 30)
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

        BillDetails billDetails = rideService.generateBill("ride123").orElse(null);

        assertNotNull(billDetails);
        assertEquals(new BigDecimal("96.55"), billDetails.getTotalFare());
        assertEquals("D1", billDetails.getDriverId());
        assertEquals("ride123", billDetails.getRideId());
    }
}