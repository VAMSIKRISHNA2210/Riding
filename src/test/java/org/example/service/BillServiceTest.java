package org.example.service;

import org.example.model.Driver;
import org.example.model.Ride;
import org.example.model.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillServiceTest {

    private BillService billService;

    @BeforeEach
    void setUp() {
        billService = new BillService();
    }

    @Test
    void testGenerateBill() {
        Rider rider = new Rider("R1", "Alice", 10.5, 20.5);
        Driver driver = new Driver("D1", "John", 10.0, 20.0);
        Ride ride = new Ride("Ride1", rider, driver);

        // Complete the ride
        ride.completeRide(15.0, 25.0, 30);

        // Generate the bill (no exceptions should be thrown)
        billService.generateBill(ride);
    }
}
