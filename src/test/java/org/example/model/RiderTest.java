package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RiderTest {

    private Rider rider;

    @BeforeEach
    void setUp() {
        rider = new Rider("R1", "Alice", 15.5, 25.5);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("R1", rider.getId());
        assertEquals("Alice", rider.getName());
        assertEquals(15.5, rider.getLatitude());
        assertEquals(25.5, rider.getLongitude());
    }

    @Test
    void testSetters() {
        rider.setId("R2");
        rider.setName("Bob");
        rider.setLatitude(20.0);
        rider.setLongitude(30.0);

        assertEquals("R2", rider.getId());
        assertEquals("Bob", rider.getName());
        assertEquals(20.0, rider.getLatitude());
        assertEquals(30.0, rider.getLongitude());
    }

    @Test
    void testAddPreferredDriver() {
        rider.addPreferredDriver("D1");
        Set<String> preferredDrivers = rider.getPreferredDrivers();

        assertTrue(preferredDrivers.contains("D1"));
        assertEquals(1, preferredDrivers.size());
    }

    @Test
    void testValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Rider invalidRider = new Rider("", "", null, null);
        Set violations = validator.validate(invalidRider);

        assertFalse(violations.isEmpty());
    }
}
