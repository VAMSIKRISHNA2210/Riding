package org.example.controller;

import org.example.model.Ride;
import org.example.service.BillService;
import org.example.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RideControllerTest {

    @Mock
    private RideService rideService;

    @Mock
    private BillService billService;

    @InjectMocks
    private RideController rideController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartRide() {
        // Arrange
        String rideId = "Ride1";
        String riderId = "R1";
        String driverId = "D1";

        Ride mockRide = mock(Ride.class);
        when(rideService.startRide(rideId, riderId, driverId)).thenReturn(mockRide);

        // Act
        ResponseEntity<String> response = rideController.startRide(rideId, riderId, driverId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride started successfully", response.getBody());
        verify(rideService, times(1)).startRide(rideId, riderId, driverId);
    }

    @Test
    void testStopRide() {
        // Arrange
        String rideId = "Ride1";
        double endLatitude = 15.5;
        double endLongitude = 25.5;
        int duration = 30;

        doNothing().when(rideService).stopRide(rideId, endLatitude, endLongitude, duration);

        // Act
        ResponseEntity<String> response = rideController.stopRide(rideId, endLatitude, endLongitude, duration);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride stopped successfully", response.getBody());
        verify(rideService, times(1)).stopRide(eq(rideId), eq(endLatitude), eq(endLongitude), eq(duration));
    }

    @Test
    void testGenerateBillSuccess() {
        // Arrange
        String rideId = "Ride1";
        Ride mockRide = mock(Ride.class);

        Map<String, Ride> ridesMap = new HashMap<>();
        ridesMap.put(rideId, mockRide);

        when(rideService.getRides()).thenReturn(ridesMap);
        when(mockRide.isCompleted()).thenReturn(true);
        doNothing().when(billService).generateBill(mockRide);

        // Act
        ResponseEntity<String> response = rideController.generateBill(rideId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bill generated successfully", response.getBody());
        verify(billService, times(1)).generateBill(mockRide);
    }

    @Test
    void testGenerateBillInvalidRide() {
        // Arrange
        String invalidRideId = "InvalidRide";

        when(rideService.getRides()).thenReturn(new HashMap<>());

        // Act
        ResponseEntity<String> response = rideController.generateBill(invalidRideId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or incomplete ride", response.getBody());
    }

    @Test
    void testGenerateBillIncompleteRide() {
        // Arrange
        String rideId = "IncompleteRide";
        Ride mockRide = mock(Ride.class);

        Map<String, Ride> ridesMap = new HashMap<>();
        ridesMap.put(rideId, mockRide);

        when(rideService.getRides()).thenReturn(ridesMap);
        when(mockRide.isCompleted()).thenReturn(false);

        // Act
        ResponseEntity<String> response = rideController.generateBill(rideId);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or incomplete ride", response.getBody());
    }
}
