# Java Backend Assignment 2025

This project is a Spring Boot backend system built for the Java Backend Interview Assignment and Reactjs Assessment (Intermediate)

## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- SQL Server Express
- Hibernate
- Postman

## Database
SQL Server Express  
Database Name: TESTDB  
Script:
  CREATE DATABASE TESTDB;

## Configuration
Update application.properties:
  spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=TESTDB;encrypt=true;trustServerCertificate=true
  spring.datasource.username=sa
  spring.datasource.password=TO_YOUR_PASSWORD

## How to Run
  mvn clean install
  mvn spring-boot:run

## Logging
All request and response details are logged via a Spring Filter
  LogPath: AssessmentApplication/logs/application.log
  
## Postman
Import the provided Postman collection:
  LowChiHaw.postman_collection
