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
    
  * Postman Agent

## Setup Instructions
### Prerequisites

  * Install Java 17 or higher.

  * Install Maven.

  * Install Postman Agent 

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

  * Fields: id, latitude, longitude.

  * Includes getters, setters, and validation annotations.

  #### Rider.java

  * Represents a rider in the system.

  * Fields: id, latitude, longitude.

  * Includes getters, setters, and validation annotations.

  #### Ride.java

  * Represents a ride in the system.

  * Fields: rideId, rider, driver, and additional fields like completion status, duration, etc.
 
### Controller Classes (org.example.controller)

  #### RideController.java
  
  * Handles endpoints related to drivers (/drivers/add).
    
  * Handles endpoints related to riders (/riders/add).

  * Handles endpoints for starting/stopping rides and generating bills (/rides/start, /rides/stop, /rides/bill).

### Service Classes (org.example.service)

  #### RideService.java

  * Contains business logic for managing drivers, riders, rides, generating bills and their interactions.


### Main.java

  * Provides an interactive command-line interface for managing drivers, riders, rides, and bills.

### RestApiApplication.java

  * Intilizes the Spring Boot Application

## Component Diagram 

<img src="https://github.com/user-attachments/assets/852eceae-238d-48d4-b6d5-bad5913d821c" alt="Component Diagram" width="500" height="650">


## Run the Application

### To run the application interactively:

```
mvn clean install

```
### To run in stdin/stdout

```
java -jar Ride-1.0-SNAPSHOT.jar cli
```
### To run in REST API 

```
java -jar Ride-1.0-SNAPSHOT.jar
```

Example Usage

Here’s an example of how you can interact with the application in stdin/stdout:
  * ADD DRIVER
    
    ```
    ADD_DRIVER D1 0 0
    ```
  * ADD RIDER
    
    ```
    ADD_RIDER R1 1 1
    ```
  * MATCH RIDER
    ```
    MATCH R1
    ```
  * START RIDE
    
    ```
    START_RIDE RIDE1 1 R1
    ```
  * STOP RIDE
    
    ```
    STOP_RIDE RIDE1 4 4 10
    ```
  * GENERATE A BILL
    
    ```
    BILL RIDE1
    ```
    
Here’s an example of how you can interact with the application in REST API in Postman Agent:

  * ADD DRIVER ( POST method )
    
    ```
    http://localhost:8080/api/rides/drivers/add?id=D1&latitude=12.9716&longitude=77.5946
    ```
  * ADD RIDER ( POST method )
    
    ```
    http://localhost:8080/api/rides/riders/add?id=R1&latitude=12.9352&longitude=77.6245
    ```
  * MATCH RIDER (GET method)

    ```
    http://localhost:8080/api/rides/match/R1
    ```
  * START RIDE (POST method)
    
    ```
    http://localhost:8080/api/rides/start?rideId=RIDE123&n=1&riderId=R1
    ```
  * STOP RIDE ( POST method )
    
    ```
    http://localhost:8080/api/rides/stop?rideId=RIDE123&endLatitude=12.9300&endLongitude=77.6200&duration=15
    ```
  * GENERATE A BILL (GET method)
    
    ```
    http://localhost:8080/api/rides/bill/RIDE123
    ```


