package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.RideController;
import org.example.service.BillDetails;
import org.example.service.RideService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RideController.class)
class RideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RideService rideService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddDriver() throws Exception {
        // Arrange
        doNothing().when(rideService).addDriver("D1", 10.5, 20.3);

        // Act & Assert
        mockMvc.perform(post("/api/rides/drivers/add")
                        .param("id", "D1")
                        .param("latitude", "10.5")
                        .param("longitude", "20.3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Driver added successfully."));

        verify(rideService, times(1)).addDriver("D1", 10.5, 20.3);
    }

    @Test
    void testAddRider() throws Exception {
        // Arrange
        doNothing().when(rideService).addRider("R1", 15.2, 25.4);

        // Act & Assert
        mockMvc.perform(post("/api/rides/riders/add")
                        .param("id", "R1")
                        .param("latitude", "15.2")
                        .param("longitude", "25.4")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Rider added successfully."));

        verify(rideService, times(1)).addRider("R1", 15.2, 25.4);
    }

    @Test
    void testMatchRider() throws Exception {
        // Arrange
        when(rideService.matchRider("R1")).thenReturn(Arrays.asList("D1", "D2", "D3"));

        // Act & Assert
        mockMvc.perform(get("/api/rides/match/R1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"D1\",\"D2\",\"D3\"]"));

        verify(rideService, times(1)).matchRider("R1");
    }

    @Test
    void testStartRideValid() throws Exception {
        // Arrange
        when(rideService.startRide("ride123", 1, "R1")).thenReturn("ride123");

        // Act & Assert
        mockMvc.perform(post("/api/rides/start")
                        .param("rideId", "ride123")
                        .param("n", "1")
                        .param("riderId", "R1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("ride123"));

        verify(rideService, times(1)).startRide("ride123", 1, "R1");
    }

    @Test
    void testStartRideInvalid() throws Exception {
        // Arrange
        when(rideService.startRide("ride123", 1, "invalidRider"))
                .thenThrow(new IllegalArgumentException("Invalid ride or already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/rides/start")
                        .param("rideId", "ride123")
                        .param("n", "1")
                        .param("riderId", "invalidRider")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid ride or already exists"));

        verify(rideService, times(1)).startRide("ride123", 1, "invalidRider");
    }

    @Test
    void testStopRideValid() throws Exception {
        // Arrange
        when(rideService.stopRide("ride123", 30.0, 40.0, 15))
                .thenReturn("ride123");

        // Act & Assert
        mockMvc.perform(post("/api/rides/stop")
                        .param("rideId", "ride123")
                        .param("endLatitude", "30.0")
                        .param("endLongitude", "40.0")
                        .param("duration", "15")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("ride123"));

        verify(rideService, times(1)).stopRide("ride123", 30.0, 40.0, 15);
    }

    @Test
    void testStopRideInvalid() throws Exception {
        // Arrange
        when(rideService.stopRide("invalidRide", 30.0, 40.0, 15))
                .thenThrow(new IllegalArgumentException("Invalid or already completed ride"));

        // Act & Assert
        mockMvc.perform(post("/api/rides/stop")
                        .param("rideId", "invalidRide")
                        .param("endLatitude", "30.0")
                        .param("endLongitude", "40.0")
                        .param("duration", "15")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or already completed ride"));

        verify(rideService, times(1)).stopRide("invalidRide", 30.0, 40.0, 15);
    }

    @Test
    void testGenerateBillValid() throws Exception {
        // Arrange
        BillDetails billDetails = new BillDetails("ride123", "driver123", new BigDecimal("200.00"));
        when(rideService.generateBill("ride123")).thenReturn(Optional.of(billDetails));

        // Act & Assert
        mockMvc.perform(get("/api/rides/bill/ride123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Total Bill for Ride ID ride123 with Driver ID driver123 is 200.00"));

        verify(rideService, times(1)).generateBill("ride123");
    }

    @Test
    void testGenerateBillInvalid() throws Exception {
        // Arrange
        when(rideService.generateBill("invalidRide")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/rides/bill/invalidRide")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or incomplete ride."));

        verify(rideService, times(1)).generateBill("invalidRide");
    }
}
