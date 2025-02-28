package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverTest {

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = new Driver("D1", "John", 10.0, 20.0);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("D1", driver.getId());
        assertEquals("John", driver.getName());
        assertEquals(10.0, driver.getLatitude());
        assertEquals(20.0, driver.getLongitude());
        assertTrue(driver.isAvailable());
    }

    @Test
    void testSetters() {
        driver.setId("D2");
        driver.setName("Jane");
        driver.setLatitude(15.0);
        driver.setLongitude(25.0);
        driver.setAvailable(false);

        assertEquals("D2", driver.getId());
        assertEquals("Jane", driver.getName());
        assertEquals(15.0, driver.getLatitude());
        assertEquals(25.0, driver.getLongitude());
        assertFalse(driver.isAvailable());
    }

    @Test
    void testAddRating() {
        driver.addRating(5);
        driver.addRating(3);

        assertEquals(4.0, driver.getRating(), 0.01); // Average rating
    }
}
