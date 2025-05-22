# Ride System

The Ride System is a Java-based application built with Spring Boot that allows users to manage drivers, riders, and rides. The system supports two modes of operation:

REST API Mode: Interact with the system using RESTful API endpoints.

CLI Mode: Interact with the system using a command-line interface.

## View the Application here
 ```
 riding.onrender.com/swagger-ui.html
 ```

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

## Component Diagram 

<img src="https://github.com/user-attachments/assets/4a80f6bb-f492-42f4-9048-a943f00bff69" alt="Component Diagram" width="500" height="650">


## Run the Application

### To run the application interactively:

```
mvn clean install
```
### To run in stdin/stdout

```
cd target
java -jar Ride-1.0-SNAPSHOT.jar cli
```
### To run in REST API 

```
cd target
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

### To run to the DockerFile

  * Clone the Repository
    
  * Create the Docker Image
    ```
    docker build -t ride-app:latest .
    ```
    
  * Run the Docker Container
    ```
    docker run -p 8080:8080 ride-app:latest
    ```

