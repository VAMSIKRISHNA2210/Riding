package org.example;

public class BillService {
    public void generateBill(Ride ride) {
        if (ride == null || !ride.isCompleted()) {
            System.out.println("INVALID_RIDE_FOR_BILLING");
            return;
        }

        double distanceFare = 10 * distance(ride.getRider().getLatitude(), ride.getRider().getLongitude(),
                ride.getDriver().getLatitude(), ride.getDriver().getLongitude());
        double totalFare = 50 + distanceFare; // Base fare + distance fare
        System.out.printf("BILL: %.2f%n", totalFare);
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}