package org.example.controller;

import org.example.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals("Driver added successfully", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).addDriver("D1", 10.5, 20.3);
    }

    @Test
    void testAddRider() {
        ResponseEntity<String> response = rideController.addRider("R1", 15.2, 25.4);

        assertEquals("Rider added successfully", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).addRider("R1", 15.2, 25.4);
    }


    @Test
    void testMatchRider() {
        List<String> expectedDrivers = Arrays.asList("D1", "D2", "D3");
        when(rideService.matchRider("R1")).thenReturn(expectedDrivers);

        ResponseEntity<List<String>> response = rideController.matchRider("R1");

        assertEquals(expectedDrivers, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).matchRider("R1");
    }

    @Test
    void testStartRide() {
        when(rideService.startRide("ride123", 1, "R1")).thenReturn("RIDE_STARTED ride123");

        ResponseEntity<String> response = rideController.startRide("ride123", 1, "R1");

        assertEquals("RIDE_STARTED ride123", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).startRide("ride123", 1, "R1");
    }

    @Test
    void testStartRideInvalid() {
        when(rideService.startRide("ride123", 1, "R1")).thenReturn("INVALID_RIDE");

        ResponseEntity<String> response = rideController.startRide("ride123", 1, "R1");

        assertEquals("Invalid ride or rider ID", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(rideService, times(1)).startRide("ride123", 1, "R1");
    }

    @Test
    void testStopRide() {
        when(rideService.stopRide("ride123", 100, 200, 30)).thenReturn("RIDE_STOPPED ride123");

        ResponseEntity<String> response = rideController.stopRide("ride123", 100, 200, 30);

        assertEquals("RIDE_STOPPED ride123", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).stopRide("ride123", 100, 200, 30);
    }

    @Test
    void testStopRideInvalidRide() {
        when(rideService.stopRide("ride123", 100, 200, 30)).thenReturn("INVALID_RIDE");

        ResponseEntity<String> response = rideController.stopRide("ride123", 100, 200, 30);

        assertEquals("Invalid ride", response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(rideService, times(1)).stopRide("ride123", 100, 200, 30);
    }

    @Test
    void testGenerateBill() {
        when(rideService.generateBillForApi("ride123")).thenReturn("BILL ride123 D1 200.00");

        ResponseEntity<String> response = rideController.generateBill("ride123");

        assertEquals("BILL ride123 D1 200.00", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(rideService, times(1)).generateBillForApi("ride123");
    }
}
