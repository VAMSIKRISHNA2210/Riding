package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {

    @Test
    void testDriverCreation() {
        Driver driver = new Driver("D1", 10.0, 20.0);
        assertEquals("D1", driver.getId());
        assertEquals(10.0, driver.getLatitude());
        assertEquals(20.0, driver.getLongitude());
        assertTrue(driver.isAvailable());
    }

    @Test
    void testSetAvailable() {
        Driver driver = new Driver("D1", 10.0, 20.0);
        driver.setAvailable(false);
        assertFalse(driver.isAvailable());
    }

    @Test
    void testUpdateLocation() {
        Driver driver = new Driver("D1", 10.0, 20.0);
        driver.updateLocation(30.0, 40.0);
        assertEquals(30.0, driver.getLatitude());
        assertEquals(40.0, driver.getLongitude());
    }
}
