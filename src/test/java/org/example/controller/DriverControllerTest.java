package org.example.controller;

import org.example.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DriverControllerTest {

    @Mock
    private RideService rideService;

    @InjectMocks
    private DriverController driverController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDriver() {
        // Arrange
        String id = "D1";
        String name = "John";
        double latitude = 10.08;
        double longitude = 20.0;

        doNothing().when(rideService).addDriver(id, name, latitude, longitude);

        // Act
        ResponseEntity<String> response = driverController.addDriver(id, name, latitude, longitude);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Driver added successfully", response.getBody());
        verify(rideService, times(1)).addDriver(id, name, latitude, longitude);
    }
}
