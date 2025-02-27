package org.example.service;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RideServiceTest {

    private RideService rideService;

    @BeforeEach
    void setUp() {
        rideService = new RideService();
    }

    @Test
    void testAddDriver() {
        rideService.addDriver("D1", "John", 10.0, 20.0);

        Driver driver = rideService.getDrivers().get("D1");
        assertNotNull(driver);
        assertEquals("John", driver.getName());
        assertTrue(driver.isAvailable());
    }

    @Test
    void testAddRider() {
        rideService.addRider("R1", "Alice", 15.0, 25.0);

        Rider rider = rideService.getRiders().get("R1");
        assertNotNull(rider);
        assertEquals("Alice", rider.getName());
    }

    @Test
    void testStartRide() {
        rideService.addDriver("D1", "John", 10.0, 20.0);
        rideService.addRider("R1", "Alice", 15.0, 25.0);

        Ride ride = rideService.startRide("Ride1", "R1", "D1");

        assertNotNull(ride);
        assertFalse(ride.getDriver().isAvailable());
    }
}
