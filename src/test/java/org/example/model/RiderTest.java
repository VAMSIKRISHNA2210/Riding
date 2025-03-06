package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiderTest {

    @Test
    void testRiderCreation() {
        Rider rider = new Rider("R1", 15.0, 25.0);
        assertEquals("R1", rider.getId());
        assertEquals(15.0, rider.getLatitude());
        assertEquals(25.0, rider.getLongitude());
    }

    @Test
    void testUpdateLocation() {
        Rider rider = new Rider("R1", 15.0, 25.0);
        rider.updateLocation(35.0, 45.0);
        assertEquals(35.0, rider.getLatitude());
        assertEquals(45.0, rider.getLongitude());
    }
}

