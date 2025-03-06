package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RideTest {

    @Test
    void testRideCreation() {
        Driver driver = new Driver("D1", 10.0, 20.0);
        Rider rider = new Rider("R1", 15.0, 25.0);
        Ride ride = new Ride("RIDE1", driver, rider);

        assertEquals("RIDE1", ride.getId());
        assertEquals(driver, ride.getDriver());
        assertEquals(rider, ride.getRider());
        assertFalse(ride.isCompleted());
    }

    @Test
    void testEndRide() {
        Driver driver = new Driver("D1", 10.0, 20.0);
        Rider rider = new Rider("R1", 15.0, 25.0);
        Ride ride = new Ride("RIDE1", driver, rider);

        ride.endRide(30.0, 40.0, 20);
        assertTrue(ride.isCompleted());
        assertEquals(20, ride.getDuration());
    }

    @Test
    void testRideStartAndEndCoordinates() {
        Driver driver = new Driver("D1", 10.0, 20.0);
        Rider rider = new Rider("R1", 15.0, 25.0);
        Ride ride = new Ride("RIDE1", driver, rider);

        // Verify starting coordinates
        assertEquals(15.0, ride.getStartLatitude());
        assertEquals(25.0, ride.getStartLongitude());

        // End the ride and verify ending coordinates
        ride.endRide(30.0, 40.0, 20);
        assertEquals(30.0, ride.getEndLatitude());
        assertEquals(40.0, ride.getEndLongitude());
    }
}
