package org.example.service;

import org.example.model.Driver;
import org.example.model.Rider;
import org.example.model.Ride;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RideServiceTest {

    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideService = new RideService();
    }

    @Test
    void testAddDriver() {
        // Act
        rideService.addDriver("D1", "John", 10.0, 20.0);

        // Assert
        Map<String, Driver> drivers = rideService.getDrivers();
        Driver driver = drivers.get("D1");

        assertNotNull(driver);
        assertEquals("John", driver.getName());
        assertTrue(driver.isAvailable());
    }

    @Test
    void testAddRider() {
        // Act
        rideService.addRider("R1", "Alice", 15.5, 25.5);

        // Assert
        Map<String, Rider> riders = rideService.getRiders();
        Rider rider = riders.get("R1");

        assertNotNull(rider);
        assertEquals("Alice", rider.getName());
    }

    @Test
    void testStartRide() {
        // Arrange
        rideService.addDriver("D1", "John", 10.0, 20.0);
        rideService.addRider("R1", "Alice", 15.5, 25.5);

        // Act
        Ride ride = rideService.startRide("Ride1", "R1", "D1");

        // Assert
        assertNotNull(ride);
        assertEquals("Ride1", ride.getRideId());
        assertFalse(ride.getDriver().isAvailable()); // Driver should be unavailable during the ride
    }

    @Test
    void testStopRide() {
        // Arrange
        rideService.addDriver("D1", "John", 10.0, 20.0);
        rideService.addRider("R1", "Alice", 15.5, 25.5);

        Ride ride = rideService.startRide("Ride1", "R1", "D1");

        // Act: Stop the ride with valid inputs
        rideService.stopRide("Ride1", 16.0, 26.0, 30);

        // Assert
        assertTrue(ride.isCompleted());
        assertEquals(30, ride.getDuration());
    }

    @Test
    void testStopInvalidRide() {
        // Arrange: Attempt to stop a non-existent or incomplete ride

        // Act & Assert: Should throw IllegalArgumentException for invalid rides
        assertThrows(IllegalArgumentException.class, () -> rideService.stopRide("InvalidRide", 16.0, 26.0, 30));
    }

    @Test
    void testMatchDrivers() {
        // Arrange
        rideService.addDriver("D1", "John", 10.0, 20.0);
        rideService.addDriver("D2", "Jane", 12.0, 22.0);
        rideService.addDriver("D3", "Jake", 50.0, 50.0); // Far away driver
        rideService.addRider("R1", "Alice", 11.0, 21.0);

        // Act: Match drivers within a radius of ~5 km (Euclidean distance)
        List<Driver> matchedDrivers = rideService.matchDrivers("R1");

        // Assert: Only nearby drivers should be matched (D1 and D2)
        assertEquals(2, matchedDrivers.size());
    }

}
