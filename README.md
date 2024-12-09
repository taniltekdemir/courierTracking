
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

## Optimization
- **Grid-based Filtering**: Store locations are grouped into grids to reduce computation and improve performance when checking proximity of couriers.
