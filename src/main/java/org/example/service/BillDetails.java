package org.example.service;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * Represents details about a bill generated for a completed ride.
 */
@Getter
public class BillDetails {

    private final String rideId;
    private final String driverId;
    private final BigDecimal totalFare;

    public BillDetails(String rideId, String driverId, BigDecimal totalFare) {
        this.rideId = rideId;
        this.driverId = driverId;
        this.totalFare = totalFare;
    }

}
