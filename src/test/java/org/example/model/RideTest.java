package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RideTest {

    private Rider rider;
    private Driver driver;
    private Ride ride;

    @BeforeEach
    void setUp() {
        rider = new Rider("R1", "Alice", 15.5, 25.5);
        driver = new Driver("D1", "John", 10.0, 20.0);
        ride = new Ride("Ride1", rider, driver);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Ride1", ride.getRideId());
        assertEquals(rider, ride.getRider());
        assertEquals(driver, ride.getDriver());
        assertFalse(ride.isCompleted());
    }

    @Test
    void testCompleteRide() {
        // Act: Complete the ride with specific values
        ride.completeRide(16.0, 26.0, 30);

        // Assert: Verify that all fields are updated correctly
        assertTrue(ride.isCompleted());
        assertEquals(16.0, ride.getEndLatitude());
        assertEquals(26.0, ride.getEndLongitude());
        assertEquals(30, ride.getDuration());
        assertTrue(driver.isAvailable()); // Driver should be available after completing the ride
    }
}