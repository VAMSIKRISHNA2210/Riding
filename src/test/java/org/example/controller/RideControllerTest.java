package org.example.controller;

import org.example.service.BillDetails;
import org.example.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideControllerTest {

    @Mock
    private RideService rideService;

    @InjectMocks
    private RideController rideController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDriver() {
        ResponseEntity<String> response = rideController.addDriver("D1", 10.5, 20.3);

        assertEquals("Driver added successfully.", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).addDriver("D1", 10.5, 20.3);
    }

    @Test
    void testAddRider() {
        ResponseEntity<String> response = rideController.addRider("R1", 15.2, 25.4);

        assertEquals("Rider added successfully.", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).addRider("R1", 15.2, 25.4);
    }

    @Test
    void testMatchRider() {
        List<String> expectedDrivers = Arrays.asList("D1", "D2", "D3");
        when(rideService.matchRider("R1")).thenReturn(expectedDrivers);

        ResponseEntity<?> response = rideController.matchRider("R1");
        @SuppressWarnings("unchecked")
        List<String> actualDrivers = (List<String>) response.getBody();

        assertEquals(expectedDrivers, actualDrivers);
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).matchRider("R1");
    }


    @Test
    void testStartRideValid() {
        when(rideService.startRide("ride123", 1, "R1")).thenReturn("ride123");

        ResponseEntity<String> response = rideController.startRide("ride123", 1, "R1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ride123", response.getBody());
        verify(rideService, times(1)).startRide("ride123", 1, "R1");
    }

    @Test
    void testStartRideInvalid() {
        when(rideService.startRide("ride123", 1, "invalidRider"))
                .thenThrow(new IllegalArgumentException("Invalid ride or already exists"));

        ResponseEntity<String> response = rideController.startRide("ride123", 1, "invalidRider");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid ride or already exists", response.getBody());
        verify(rideService, times(1)).startRide("ride123", 1, "invalidRider");
    }

    @Test
    void testStopRideValid() {
        when(rideService.stopRide("ride123", 30.0, 40.0, 15))
                .thenReturn("ride123");

        ResponseEntity<String> response = rideController.stopRide("ride123", 30.0, 40.0, 15);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ride123", response.getBody());
        verify(rideService, times(1)).stopRide("ride123", 30.0, 40.0, 15);
    }

    @Test
    void testStopRideInvalid() {
        when(rideService.stopRide("invalidRide", 30.0, 40.0, 15))
                .thenThrow(new IllegalArgumentException("Invalid or already completed ride"));

        ResponseEntity<String> response = rideController.stopRide("invalidRide", 30.0, 40.0, 15);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or already completed ride", response.getBody());
        verify(rideService, times(1)).stopRide("invalidRide", 30.0, 40.0, 15);
    }

    @Test
    void testGenerateBillValid() {
        BillDetails billDetails = new BillDetails("ride123", "driver123", new BigDecimal("200.00"));
        when(rideService.generateBill("ride123")).thenReturn(java.util.Optional.of(billDetails));

        ResponseEntity<?> response = rideController.generateBill("ride123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Total Bill for Ride ID ride123 with Driver ID driver123 is 200.00", response.getBody());
        verify(rideService, times(1)).generateBill("ride123");
    }

    @Test
    void testGenerateBillInvalid() {
        when(rideService.generateBill("invalidRide")).thenReturn(java.util.Optional.empty());

        ResponseEntity<?> response = rideController.generateBill("invalidRide");

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or incomplete ride.", response.getBody());
        verify(rideService, times(1)).generateBill("invalidRide");
    }
}
