
# Courier Tracking Application

This project is a RESTful web application designed to manage and analyze courier geolocations. It processes streaming location data to track courier activities near stores and calculate travel distances.

## Features
- Logs courier visits when they enter a 100-meter radius around stores.
    - Re-entries within 1 minute are ignored to avoid duplicate logs.
- Provides a method to query the total travel distance of a courier:
  ```java
  Double getTotalTravelDistance(courierId, startDate, endDate);
  ```
- Optimized performance by grouping store locations into a grid structure for efficient filtering.
- Includes Postman collection for testing endpoints.

## Technologies Used
- **Java 17**
- **Spring Boot**
- **PostgreSQL** for persistent data storage
- **H2 SQL** for in-memory database testing
- **Lombok** for boilerplate code reduction
- **Docker** for containerization

## Setup Instructions

### Prerequisites
- Docker and Docker Compose installed on your system
- Java Development Kit (JDK 17)
- Maven (for building the application)

### Steps to Run the Application
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Build the Application**
   Use Maven to package the application:
   ```bash
   mvn clean package
   ```

3. **Start the Application**
   Use Docker Compose to build and run the application:
   ```bash
   docker-compose build
   docker-compose up
   ```

4. **Access the Application**
    - The application will be running on `http://localhost:<port>` (default port is specified in `application.properties`).
    - PostgreSQL will be configured and initialized automatically. You can monitoring pgAdmin dashboard. Docker-compose file contain it.

5. **Testing with Postman**
    - Import the provided **Postman collection** in the repository to test the API endpoints.
    - The collection includes examples for tracking courier locations and querying travel distances.

## Database
- **PostgreSQL** is used as the main database for persistent storage.
- **H2 SQL** is used for in-memory testing during development.

## Store Information and Singleton Design Pattern
- Store locations are sourced from the `store.json` file included in the project. This file contains pre-defined coordinates for stores.
- The **Singleton Design Pattern** is applied to ensure efficient management of the store data.
   - A singleton class is used to load and cache the store locations from `store.json` only once during the application's lifecycle. This avoids repeated file reads and enhances performance.
   - Any part of the application requiring store data retrieves it from this singleton instance.

## Event Handling and Observer Design Pattern

- After a courier's location data is recorded, an event is generated to evaluate whether the location indicates a store entry.
- This event is processed by a **listener**, which determines if the courier has entered the vicinity of a store.
- If the location corresponds to a store entry:
    - It checks whether there is an active delivery trip for the courier.
    - Depending on the evaluation, it either closes the existing trip or starts a new active trip.
    - Distance calculations and validations (e.g., time differences between entries) are also handled during this process.

### Observer Pattern Implementation
- The application attempts to utilize the **Observer Pattern**:
    - The **event listener** observes changes triggered by the generated events.
    - This ensures a decoupled architecture where the event generator and listener interact indirectly.
    - The `@EventListener` and `ApplicationEventPublisher` components provided by Spring Framework facilitate this implementation.

This approach promotes modularity and simplifies extending the application to handle additional event types in the future.


## Optimization
- **Grid-based Filtering**: Store locations are grouped into grids to reduce computation and improve performance when checking proximity of couriers.


## Integration Testing with `should_insert_100_location_together` API

- The `should_insert_100_location_together` API can be used for integration testing to validate the application's functionality for handling bulk location updates and trip calculations.
- This API performs the following operations:
    1. Creates a new courier.
    2. Simulates the courier starting at a store and records 20 sequential location updates.
    3. Every 5 locations, the courier is simulated to enter a different store.
    4. The system evaluates these entries and calculates the traveled distance for the corresponding delivery trip.

### Test Objectives
Using this API, you can:
- Verify that the courier is correctly created and associated with location updates.
- Confirm that store entries are detected accurately based on the proximity of locations to store coordinates.
- Ensure that the distance traveled for each delivery trip is calculated and recorded properly.
- Test the robustness of the event-driven architecture under a batch of location updates.

This API serves as a comprehensive tool to test core functionalities, including location logging, event handling, and trip management, in an end-to-end scenario.
