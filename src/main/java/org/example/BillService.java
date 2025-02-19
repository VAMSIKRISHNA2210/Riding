package org.example;

public class BillService {

    // Calculate the bill for a completed ride
    public void generateBill(Ride ride) {
        if (ride == null || !ride.isCompleted()) {
            System.out.println("INVALID_RIDE_FOR_BILLING");
            return;
        }

        double distanceTravelled = calculateDistance(ride);
        int duration = ride.getDuration();

        // Base fare: $50
        double baseFare = 50;

        // Distance-based fare: $6.5 per km
        double distanceFare = 6.5 * distanceTravelled;

        // Time-based fare: $2 per minute
        double timeFare = 2 * duration;

        // Total fare before tax
        double totalFare = baseFare + distanceFare + timeFare;

        // Add 20% service tax
        double totalBill = totalFare * 1.2;

        System.out.printf("BILL: %.2f%n", totalBill);
    }

    // Calculate distance of the completed ride
    private double calculateDistance(Ride ride) {
        return Math.sqrt(Math.pow(ride.getDriver().getLatitude() - ride.getRider().getLatitude(), 2)
                         + Math.pow(ride.getDriver().getLongitude() - ride.getRider().getLongitude(), 2));
    }
}
