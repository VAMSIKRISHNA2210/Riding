## Ride System

This project implements a simple ride-hailing system using Java and Maven.
Prerequisites

  1.Java JDK 11 or higher

  2.Maven 3.6 or higher

  3.Git

## Cloning and Running the Project

Clone the repository:
```
git clone https://github.com/VAMSIKRISHNA2210/Riding.git
cd Riding
```
Build the project:
```
mvn clean install
```
Run tests:
```
mvn test
```
## Project Structure
Project Structure

Here’s an overview of the files in the project:
1. Main.java

    Purpose: Entry point of the application. Handles user commands like adding drivers/riders, starting/stopping rides, generating bills, and more.

    Key Methods:

   1.main(String[] args): Initializes the application.

   2.run(String[] args): Contains the main logic for processing user commands.

2. RideService.java

    Purpose: Core service that manages drivers, riders, and rides.

    Key Responsibilities:

   1.Adding drivers and riders.

   2.Matching riders with nearby drivers.

   3.Starting and stopping rides.

   4.Managing preferred drivers and driver ratings.

3. BillService.java

    Purpose: Handles billing for completed rides.

    Key Responsibilities:

   1.Calculate ride fares based on distance and duration.

   2.Add a service tax to the total fare.

4. Driver.java

    Purpose: Represents a driver in the system.

    Attributes:

     id, name, latitude, longitude, available, rating, totalRatings.

    Key Methods:

     addRating(int rating): Updates the driver's average rating.

5. Rider.java

    Purpose: Represents a rider in the system.

    Attributes:

     id, name, latitude, longitude, preferredDrivers.

    Key Methods:

     addPreferredDriver(String driverId): Adds a driver to the rider's list of preferred drivers.

6. Ride.java

    Purpose: Represents a ride in the system.

    Attributes:

     rideId, rider, driver, startLatitude, startLongitude, endLatitude, endLongitude, duration, completed.

    Key Methods:

     completeRide(double endLatitude, double endLongitude, int duration): Marks the ride as completed.

7. MainTest.java

    Purpose: Unit tests for the application logic in Main.java.

    Coverage:

     Tests all commands handled by the Main class (e.g., adding drivers/riders, starting/stopping rides, generating bills).
     Ensures edge cases like unavailable drivers or incomplete rides are handled correctly.

## Run the Application

To run the application interactively:

```
mvn exec:java -Dexec.mainClass="org.example.Main"
```
Example Usage

Here’s an example of how you can interact with the application:
```
ADD_DRIVER D1 John 10.0 20.0
DRIVER_ADDED

ADD_RIDER R1 Alice 15.0 25.0
RIDER_ADDED

MATCH R1
NO_DRIVERS_AVAILABLE

START_RIDE Ride1 R1 D1
RIDE_STARTED Ride1

STOP_RIDE Ride1 15.5 25.5 30
RIDE_STOPPED Ride1

BILL Ride1
BILL: $120.71

RATE_DRIVER Ride1 5
DRIVER_RATED

ADD_PREFERRED_DRIVER R1 D1
PREFERRED_DRIVER_ADDED
```
