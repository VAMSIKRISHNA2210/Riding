package org.example.controller;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;
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

        // Mocking startRide to return a Ride object
        Ride mockRide = new Ride(rideId, new Rider(riderId, "Alice", 10.0, 20.0), new Driver(driverId, "John", 15.0, 25.0));
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
        double endLatitude = 15.0;
        double endLongitude = 25.0;
        int duration = 30;

        doNothing().when(rideService).stopRide(rideId, endLatitude, endLongitude, duration);

        // Act
        ResponseEntity<String> response = rideController.stopRide(rideId, endLatitude, endLongitude, duration);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Ride stopped successfully", response.getBody());
        verify(rideService, times(1)).stopRide(rideId, endLatitude, endLongitude, duration);
    }

    @Test
    void testGenerateBill() {
        // Arrange
        String rideId = "Ride1";
        Ride mockRide = mock(Ride.class);

        Map<String, Ride> mockRides = new HashMap<>();
        mockRides.put(rideId, mockRide);

        when(rideService.getRides()).thenReturn(mockRides);
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
        assertEquals("Invalid ride for billing", response.getBody());
    }
}
