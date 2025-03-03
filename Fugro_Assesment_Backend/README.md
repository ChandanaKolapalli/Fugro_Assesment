# Offshore Ground Sampling Management System(Backend)

## Overview
This is a full-stack application to manage offshore ground sampling data for environmental studies. It allows users to create, retrieve, update, and delete samples from the system.

## Features
- RESTful API for CRUD operations on samples.
- Fetch Average of water content for all the samples
- Fetch Threshold data for all the samples
- Fetch All the location details
- Basic Authentication to access the RESTful API's
- Perform Unit tests
- DataInitialiser to add the locations to the database.

## Technologies Used
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Maven

## Prerequisites
Make sure you have the following installed:
- Java 17 or later
- Maven 3+
- An IDE (IntelliJ IDEA, Eclipse, or STS)

## Getting Started

### Clone the Repository
```sh
git clone https://github.com/yourusername/spring-boot-book-api.git
cd spring-boot-book-api
```

### Build and Run the Application
```sh
mvn spring-boot:run
```

The application will start at `http://localhost:8080`.

## Configuration
Modify `src/main/resources/application.properties` to update database settings:
```properties

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.platform=h2
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
```
Modify `src/main/resources/application.properties` to configure the threshold values:
```properties
spring.furgo.ogs.WeightThreshold=2.0
spring.furgo.ogs.WaterContentThreshold=2.0
spring.furgo.ogs.ShearStrengthThreshold=2.0
```

Modify `src/main/resources/application.properties` for security configuration values:
```properties
spring.security.user.name=admin
spring.security.user.password=secret
```
##API End Points
| Method | Endpoint                   | Description                |
|--------|----------------------------|----------------------------|
| GET    | /samples/list              | Get all samples            |
| POST   | /samples/add               | Add new sample             |
| PUT    | /samples/update/{id}       | Update sample              |
| DELETE | /samples/delete{id}        | Delete sample              |
| GET    | /samples/avgwatercontent	  | Get average water content  |
| GET    | /samples/locations         | Get all locations          |
| GET	 | /samples/thresholdvalues	  |	Get Threshold values	   |


## Running Tests
Run the following command to execute tests:
```sh
mvn test
```