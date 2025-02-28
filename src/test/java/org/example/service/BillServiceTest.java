package org.example.service;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BillServiceTest {

    private BillService billService;

    @BeforeEach
    void setUp() {
        billService = new BillService();
    }

    @Test
    void testGenerateBillValidRide() {
        // Arrange
        Rider rider = new Rider("R1", "Alice", 15.5, 25.5);
        Driver driver = new Driver("D1", "John", 10.0, 20.0);
        Ride ride = new Ride("Ride1", rider, driver);

        ride.completeRide(16.0, 26.0, 30); // Completing the ride

        // Act & Assert: No exceptions should be thrown
        assertDoesNotThrow(() -> billService.generateBill(ride));
    }

    @Test
    void testGenerateBillInvalidRide() {
        // Arrange: Null ride or incomplete ride should throw exceptions
        Ride incompleteRide = mock(Ride.class);
        when(incompleteRide.isCompleted()).thenReturn(false);

        // Act & Assert: Invalid rides should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> billService.generateBill(null));
        assertThrows(IllegalArgumentException.class, () -> billService.generateBill(incompleteRide));
    }
}
