package org.example.service;

import org.example.model.Ride;
import org.springframework.stereotype.Service;

@Service
public class BillService {

    public void generateBill(Ride ride) {
        if (ride == null || !ride.isCompleted()) {
            throw new IllegalArgumentException("Invalid ride for billing");
        }

        double distance = Math.sqrt(Math.pow(ride.getRider().getLatitude() - ride.getDriver().getLatitude(), 2)
                + Math.pow(ride.getRider().getLongitude() - ride.getDriver().getLongitude(), 2));
        int duration = ride.getDuration();

        // Base fare: $50 + $6.5 per km + $2 per minute + 20% service tax
        double baseFare = 50 + (6.5 * distance) + (2 * duration);
        double totalFare = baseFare * 1.2; // Adding 20% service tax

        System.out.printf("BILL: %.2f%n", totalFare);
    }
}
