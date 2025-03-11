package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class RideServiceTest {
    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideService = new RideService();
    }

    @Test
    void testAddDriver() {
        rideService.addDriver("D1", 10, 20);
        rideService.addRider("R1", 11, 21);
        List<String> matches = rideService.matchRider("R1");
        assertEquals(1, matches.size());
        assertEquals("D1", matches.get(0));
    }

    @Test
    void testAddRider() {
        rideService.addRider("R1", 15, 25);
        rideService.addDriver("D1", 14, 24);
        List<String> matches = rideService.matchRider("R1");
        assertEquals(1, matches.size());
        assertEquals("D1", matches.get(0));
    }

    @Test
    void testMatchRider() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);     // ~0.14 km away
        rideService.addDriver("D2", 3, 3);     // ~0.42 km away
        rideService.addDriver("D3", 40, 40);   // ~5.66 km away, should not match
        rideService.addDriver("D4", 20, 20);   // ~2.83 km away
        rideService.addDriver("D5", 25, 25);   // ~3.54 km away

        List<String> matches = rideService.matchRider("R1");

        assertEquals(4, matches.size(), "Should match 4 drivers within 5 km");
        assertTrue(matches.contains("D1"), "D1 should be matched");
        assertTrue(matches.contains("D2"), "D2 should be matched");
        assertTrue(matches.contains("D4"), "D4 should be matched");
        assertTrue(matches.contains("D5"), "D5 should be matched");
        assertFalse(matches.contains("D3"), "D3 should not be matched as it's beyond 5 km");
    }

    @Test
    void testMatchRiderNoMatches() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 20, 20);
        List<String> matches = rideService.matchRider("R1");
        assertFalse(matches.isEmpty());
    }

    @Test
    void testMatchRiderInvalidRider() {
        List<String> matches = rideService.matchRider("R2");
        assertTrue(matches.isEmpty());
    }

    @Test
    void testStartRide() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        String result = rideService.startRide("RIDE1", 1, "R1");
        assertEquals("RIDE_STARTED RIDE1", result);
    }

    @Test
    void testStartRideInvalidRider() {
        String result = rideService.startRide("RIDE1", 1, "R2");
        assertEquals("INVALID_RIDE", result);
    }

    @Test
    void testStartRideInvalidDriverNumber() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        String result = rideService.startRide("RIDE1", 2, "R1");
        assertEquals("INVALID_RIDE", result);
    }

    @Test
    void testStartRideDuplicateRideId() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        rideService.startRide("RIDE1", 1, "R1");
        String result = rideService.startRide("RIDE1", 1, "R1");
        assertEquals("INVALID_RIDE", result);
    }

    @Test
    void testStopRide() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        rideService.startRide("RIDE1", 1, "R1");
        String result = rideService.stopRide("RIDE1", 10, 10, 30);
        assertEquals("RIDE_STOPPED RIDE1", result);
    }

    @Test
    void testStopRideInvalidRide() {
        String result = rideService.stopRide("RIDE2", 10, 10, 30);
        assertEquals("INVALID_RIDE", result);
    }

    @Test
    void testStopRideAlreadyCompleted() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        rideService.startRide("RIDE1", 1, "R1");
        rideService.stopRide("RIDE1", 10, 10, 30);
        String result = rideService.stopRide("RIDE1", 20, 20, 60);
        assertEquals("INVALID_RIDE", result);
    }

    @Test
    void testGenerateBillForApi() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        rideService.startRide("RIDE1", 1, "R1");
        rideService.stopRide("RIDE1", 10, 10, 30);

        String bill = rideService.generateBillForApi("RIDE1");
        assertTrue(bill.startsWith("BILL RIDE1 D1"), "API bill should start with 'BILL RIDE1 D1'");
    }

    @Test
    void testGenerateBillForCli() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        rideService.startRide("RIDE1", 1, "R1");
        rideService.stopRide("RIDE1", 10, 10, 30);

        String bill = rideService.generateBillForCli("RIDE1");
        assertTrue(bill.startsWith("Total Bill:"), "CLI bill should start with 'Total Bill:'");
    }


    @Test
    void testGenerateBillInvalidRide() {
        String result = rideService.generateBillForCli("RIDE2");
        assertEquals("INVALID_RIDE", result);
    }

    @Test
    void testGenerateBillIncompleteRide() {
        rideService.addRider("R1", 0, 0);
        rideService.addDriver("D1", 1, 1);
        rideService.startRide("RIDE1", 1, "R1");
        String result = rideService.generateBillForCli("RIDE1");
        assertEquals("INVALID_RIDE", result);
    }
}
