# Ride System

The Ride System is a Java-based application built with Spring Boot that allows users to manage drivers, riders, and rides. The system supports two modes of operation:

REST API Mode: Interact with the system using RESTful API endpoints.

CLI Mode: Interact with the system using a command-line interface.

## Features

  * Add and manage drivers.

  * Add and manage riders.

  * Start and stop rides.

  * Generate bills for completed rides.

  * Two modes of operation: REST API and CLI.

## Technologies Used

  * Java 17

  * Spring Boot 2.x

  * Hibernate Validator (for validation)

  * JUnit 5 (for testing)

  * Maven (for dependency management)

## Setup Instructions
### Prerequisites

  * Install Java 17 or higher.

  * Install Maven.

  * Clone the repository:
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

### Model Classes (org.example.model)

  #### Driver.java

  * Represents a driver in the system.

  * Fields: id, name, latitude, longitude.

  * Includes getters, setters, and validation annotations.

  #### Rider.java

  * Represents a rider in the system.

  * Fields: id, name, latitude, longitude.

  * Includes getters, setters, and validation annotations.

  #### Ride.java

  * Represents a ride in the system.

  * Fields: rideId, rider, driver, and additional fields like completion status, duration, etc.
 
### Controller Classes (org.example.controller)

  #### DriverController.java

  * Handles endpoints related to drivers (/drivers/add).

  * Accepts query parameters or JSON body for adding drivers.

  #### RiderController.java

  * Handles endpoints related to riders (/riders/add).

  * Accepts query parameters or JSON body for adding riders.

  #### RideController.java

  * Handles endpoints for starting/stopping rides and generating bills (/rides/start, /rides/stop, /rides/bill).

### Service Classes (org.example.service)

  #### RideService.java

  * Contains business logic for managing drivers, riders, rides, and their interactions.

  #### BillService.java

  * Responsible for calculating and generating bills for completed rides.

### CLI Class (org.example.cli)

  #### CommandLineInterface.java

  * Provides an interactive command-line interface for managing drivers, riders, rides, and bills.

## Run the Application

### To run the application interactively:

```
mvn clean install

cd target
```
### To run in stdin/stdout

```
java -jar Ride-1.0-SNAPSHOT.jar
```
### To run in REST API 

```
java -jar Ride-1.0-SNAPSHOT.jar rest
```

Example Usage

Here’s an example of how you can interact with the application in stdin/stdout:
  * ADD DRIVER
    
    ```
    ADD_DRIVER D1 John 10.0 20.0
    ```
  * ADD RIDER
    
    ```
    ADD_RIDER R1 Alice 15.0 25.0
    ```
  * START RIDE
    
    ```
    START_RIDE Ride1 R1 D1
    ```
  * STOP RIDE
    
    ```
    STOP_RIDE Ride1 15.5 25.5 30
    ```
  * GENERATE A BILL
    
    ```
    GENERATE_BILL Ride1
    ```
    
Here’s an example of how you can interact with the application in REST API:

  * ADD DRIVER
    
    ```
    curl -X POST "http://localhost:8080/drivers/add?id=D1&name=John&latitude=10.08&longitude=20.0"
    ```
  * ADD RIDER
    
    ```
    curl -X POST "http://localhost:8080/riders/add?id=R1&name=Alice&latitude=15.5&longitude=25.5"
    ```
  * START RIDE
    
    ```
    curl -X POST "http://localhost:8080/rides/start?rideId=Ride1&riderId=R1&driverId=D1"
    ```
  * STOP RIDE
    
    ```
    curl -X POST "http://localhost:8080/rides/stop?rideId=Ride1&endLatitude=16.0&endLongitude=26.0&duration=30"
    ```
  * GENERATE A BILL
    
    ```
    curl -X GET "http://localhost:8080/rides/bill?rideId=Ride1"
    ```


