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
        ResponseEntity<String> response = riderController.addRider("R1", "Alice", 15.0, 25.0);

        verify(rideService, times(1)).addRider("R1", "Alice", 15.0, 25.0);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Rider added successfully", response.getBody());
    }
}
