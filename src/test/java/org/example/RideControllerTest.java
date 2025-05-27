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
    void testCreateDriver() throws Exception {
        // Arrange
        doNothing().when(rideService).addDriver("D1", 10.5, 20.3);

        // Act & Assert
        mockMvc.perform(post("/api/drivers")
                        .param("driverId", "D1")
                        .param("latitude", "10.5")
                        .param("longitude", "20.3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Driver created successfully"))
                .andExpect(jsonPath("$.driverId").value("D1"))
                .andExpect(jsonPath("$.location.latitude").value(10.5))
                .andExpect(jsonPath("$.location.longitude").value(20.3))
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links[0].href").value("/api/drivers/D1"))
                .andExpect(jsonPath("$._links[0].rel").value("self"))
                .andExpect(jsonPath("$._links[0].method").value("GET"));
    }

    @Test
    void testCreateDriverWithError() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Invalid driver ID"))
                .when(rideService).addDriver("", 10.5, 20.3);

        // Act & Assert
        mockMvc.perform(post("/api/drivers")
                        .param("driverId", "")
                        .param("latitude", "10.5")
                        .param("longitude", "20.3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid driver ID"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testCreateRider() throws Exception {
        // Arrange
        doNothing().when(rideService).addRider("R1", 15.2, 25.4);

        // Act & Assert
        mockMvc.perform(post("/api/riders")
                        .param("riderId", "R1")
                        .param("latitude", "15.2")
                        .param("longitude", "25.4")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Rider created successfully"))
                .andExpect(jsonPath("$.riderId").value("R1"))
                .andExpect(jsonPath("$.location.latitude").value(15.2))
                .andExpect(jsonPath("$.location.longitude").value(25.4))
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links[0].href").value("/api/riders/R1"))
                .andExpect(jsonPath("$._links[1].href").value("/api/riders/R1/matches"));
    }

    @Test
    void testCreateRiderWithError() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Invalid rider ID"))
                .when(rideService).addRider("", 15.2, 25.4);

        // Act & Assert
        mockMvc.perform(post("/api/riders")
                        .param("riderId", "")
                        .param("latitude", "15.2")
                        .param("longitude", "25.4")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid rider ID"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testGetRiderMatches() throws Exception {
        // Arrange
        when(rideService.matchRider("R1")).thenReturn(Arrays.asList("D1", "D2", "D3"));

        // Act & Assert
        mockMvc.perform(get("/api/riders/R1/matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riderId").value("R1"))
                .andExpect(jsonPath("$.availableDrivers").isArray())
                .andExpect(jsonPath("$.availableDrivers[0]").value("D1"))
                .andExpect(jsonPath("$.availableDrivers[1]").value("D2"))
                .andExpect(jsonPath("$.availableDrivers[2]").value("D3"))
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.filters.maxDistance").value("default"))
                .andExpect(jsonPath("$.filters.sort").value("distance"))
                .andExpect(jsonPath("$._links").isArray());
    }

    @Test
    void testGetRiderMatchesWithFilters() throws Exception {
        // Arrange
        when(rideService.matchRider("R1")).thenReturn(Arrays.asList("D1", "D2"));

        // Act & Assert
        mockMvc.perform(get("/api/riders/R1/matches")
                        .param("maxDistance", "10.0")
                        .param("sort", "rating")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riderId").value("R1"))
                .andExpect(jsonPath("$.availableDrivers").isArray())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.filters.maxDistance").value(10.0))
                .andExpect(jsonPath("$.filters.sort").value("rating"));
    }

    @Test
    void testGetRiderMatchesNotFound() throws Exception {
        // Arrange
        when(rideService.matchRider("invalidRider"))
                .thenThrow(new IllegalArgumentException("Rider not found"));

        // Act & Assert
        mockMvc.perform(get("/api/riders/invalidRider/matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Rider not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testCreateRide() throws Exception {
        // Arrange
        when(rideService.startRide("ride123", 1, "R1")).thenReturn("ride123");

        // Act & Assert
        mockMvc.perform(post("/api/rides")
                        .param("rideId", "ride123")
                        .param("driverIndex", "1")
                        .param("riderId", "R1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Ride created successfully"))
                .andExpect(jsonPath("$.rideId").value("ride123"))
                .andExpect(jsonPath("$.riderId").value("R1"))
                .andExpect(jsonPath("$.driverIndex").value(1))
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links[0].href").value("/api/rides/ride123"))
                .andExpect(jsonPath("$._links[1].href").value("/api/rides/ride123/complete"));
    }

    @Test
    void testCreateRideInvalid() throws Exception {
        // Arrange
        when(rideService.startRide("ride123", 1, "invalidRider"))
                .thenThrow(new IllegalArgumentException("Invalid ride or already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/rides")
                        .param("rideId", "ride123")
                        .param("driverIndex", "1")
                        .param("riderId", "invalidRider")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid ride or already exists"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testCompleteRide() throws Exception {
        // Arrange
        when(rideService.stopRide("ride123", 30.0, 40.0, 15))
                .thenReturn("ride123");

        // Act & Assert
        mockMvc.perform(patch("/api/rides/ride123/complete")
                        .param("endLatitude", "30.0")
                        .param("endLongitude", "40.0")
                        .param("duration", "15")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ride completed successfully"))
                .andExpect(jsonPath("$.rideId").value("ride123"))
                .andExpect(jsonPath("$.endLocation.latitude").value(30.0))
                .andExpect(jsonPath("$.endLocation.longitude").value(40.0))
                .andExpect(jsonPath("$.duration").value(15))
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links[0].href").value("/api/rides/ride123"))
                .andExpect(jsonPath("$._links[1].href").value("/api/rides/ride123/bill"));
    }

    @Test
    void testCompleteRideInvalid() throws Exception {
        // Arrange
        when(rideService.stopRide("invalidRide", 30.0, 40.0, 15))
                .thenThrow(new IllegalArgumentException("Invalid or already completed ride"));

        // Act & Assert
        mockMvc.perform(patch("/api/rides/invalidRide/complete")
                        .param("endLatitude", "30.0")
                        .param("endLongitude", "40.0")
                        .param("duration", "15")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid or already completed ride"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testGetRideBill() throws Exception {
        // Arrange
        BillDetails billDetails = new BillDetails("ride123", "driver123", new BigDecimal("200.00"));
        when(rideService.generateBill("ride123")).thenReturn(Optional.of(billDetails));

        // Act & Assert
        mockMvc.perform(get("/api/rides/ride123/bill")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideId").value("ride123"))
                .andExpect(jsonPath("$.driverId").value("driver123"))
                .andExpect(jsonPath("$.totalFare").value(200.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$._links").isArray())
                .andExpect(jsonPath("$._links[0].href").value("/api/rides/ride123/bill"))
                .andExpect(jsonPath("$._links[1].href").value("/api/rides/ride123"));
    }

    @Test
    void testGetRideBillWithFields() throws Exception {
        // Arrange
        BillDetails billDetails = new BillDetails("ride123", "driver123", new BigDecimal("200.00"));
        when(rideService.generateBill("ride123")).thenReturn(Optional.of(billDetails));

        // Act & Assert
        mockMvc.perform(get("/api/rides/ride123/bill")
                        .param("fields", "rideId,totalFare")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideId").value("ride123"))
                .andExpect(jsonPath("$.totalFare").value(200.00));
    }

    @Test
    void testGetRideBillNotFound() throws Exception {
        // Arrange
        when(rideService.generateBill("invalidRide")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/rides/invalidRide/bill")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Invalid or incomplete ride"))
                .andExpect(jsonPath("$.rideId").value("invalidRide"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
