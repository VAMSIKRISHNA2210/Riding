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

class RiderControllerTest {

    @Mock
    private RideService rideService;

    @InjectMocks
    private RiderController riderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRider() {
        // Arrange
        String id = "R1";
        String name = "Alice";
        double latitude = 15.5;
        double longitude = 25.5;

        doNothing().when(rideService).addRider(id, name, latitude, longitude);

        // Act
        ResponseEntity<String> response = riderController.addRider(id, name, latitude, longitude);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rider added successfully", response.getBody());
        verify(rideService, times(1)).addRider(id, name, latitude, longitude);
    }
}