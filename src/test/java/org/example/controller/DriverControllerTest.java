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
        ResponseEntity<String> response = driverController.addDriver("D1", "John", 10.0, 20.0);

        verify(rideService, times(1)).addDriver("D1", "John", 10.0, 20.0);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Driver added successfully", response.getBody());
    }
}
