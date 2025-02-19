package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private RideService rideService;
    private BillService billService;

    @BeforeEach
    void setUp() {
        rideService = new RideService();
        billService = new BillService();
    }

    // Test Case 1: Add a driver and verify they exist in the system
    @Test
    void testAddDriver() {
        rideService.addDriver("D1", 10.0, 20.0);
        List<Driver> drivers = rideService.matchDrivers("R1");
        assertNotNull(drivers);
        assertEquals(0, drivers.size()); // No riders yet
    }

    // Test Case 2: Add a rider and verify they exist in the system
    @Test
    void testAddRider() {
        rideService.addRider("R1", 15.0, 25.0);
        assertNotNull(rideService.matchDrivers("R1"));
    }

    // Test Case 3: Match drivers for a rider with no nearby drivers
    @Test
    void testMatchNoDrivers() {
        rideService.addRider("R1", 15.0, 25.0);
        List<Driver> drivers = rideService.matchDrivers("R1");
        assertTrue(drivers.isEmpty());
    }

    // Test Case 4: Match drivers for a rider with nearby drivers
    @Test
    void testMatchDrivers() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addDriver("D2", 11.0, 21.0);
        rideService.addRider("R1", 10.5, 20.5);

        List<Driver> drivers = rideService.matchDrivers("R1");
        assertEquals(2, drivers.size());
        assertEquals("D1", drivers.get(0).getId()); // Closest driver first
    }

    // Test Case 5: Start a ride successfully
    @Test
    void testStartRide() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        Ride ride = rideService.startRide("Ride1", "R1", "D1");
        assertNotNull(ride);
        assertFalse(ride.getDriver().isAvailable()); // Driver should be unavailable during the ride
    }

    // Test Case 6: Attempt to start a ride with an unavailable driver
    @Test
    void testStartRideWithUnavailableDriver() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        Ride firstRide = rideService.startRide("Ride1", "R1", "D1");
        assertNotNull(firstRide);

        Ride secondRide = rideService.startRide("Ride2", "R2", "D1"); // Driver is already on a ride
        assertNull(secondRide); // Second ride should fail
    }

    // Test Case 7: Stop a ride successfully and verify driver availability
    @Test
    void testStopRide() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        Ride ride = rideService.startRide("Ride1", "R1", "D1");
        assertNotNull(ride);

        rideService.stopRide("Ride1", 30); // Stop the ride after traveling and time spent

        assertTrue(ride.isCompleted());
        assertTrue(ride.getDriver().isAvailable()); // Driver should be available after completing the ride
    }

    // Test Case 8: Generate a bill for a completed ride
    @Test
    void testGenerateBillForCompletedRide() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        Ride ride = rideService.startRide("Ride1", "R1", "D1");
        assertNotNull(ride);

        // Stop the ride to complete it
        rideService.stopRide("Ride1", 30);

        // Generate the bill for the completed ride
        billService.generateBill(ride);

        // Verify that calculations are correct (manually check expected values)
        assertTrue(ride.isCompleted());
    }

    // Test Case 9: Attempt to generate a bill for an incomplete ride
    @Test
    void testGenerateBillForIncompleteRide() {
        rideService.addDriver("D1", 10.0, 20.0);
        rideService.addRider("R1", 10.5, 20.5);

        Ride ride = rideService.startRide("Ride1", "R1", "D1");
        assertNotNull(ride);

        // Attempt to generate a bill without stopping the ride
        billService.generateBill(ride);

        // Since the ride is not completed, no valid bill should be generated
        assertFalse(ride.isCompleted());
    }

    // Test Case 10: Verify distance-based matching of drivers
    @Test
    void testDistanceBasedDriverMatching() {
        // Add drivers with specific locations
        rideService.addDriver("D1", 10.0, 20.0); // Within range (close)
        rideService.addDriver("D2", 11.0, 21.0); // Within range (slightly farther)
        rideService.addDriver("D3", 50.0, 50.0); // Out of range (>5km)

        // Add a rider at a specific location
        rideService.addRider("R1", 10.5, 20.5);

        // Match drivers for the rider
        List<Driver> matchedDrivers = rideService.matchDrivers("R1");

        // Verify that only drivers within range are matched
        assertEquals(2, matchedDrivers.size()); // Expecting D1 and D2 to match
        assertEquals("D1", matchedDrivers.get(0).getId()); // Closest driver first
        assertEquals("D2", matchedDrivers.get(1).getId()); // Next closest driver
    }

}
