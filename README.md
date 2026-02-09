# Java Backend Assignment 2025 and Reactjs Assessment (Intermediate)

This project is a Spring Boot backend system built for the Java Backend Interview Assignment and Reactjs Assessment (Intermediate)

## Database
Microsoft SQL Server
Database Name: TESTDB  
Script:
  CREATE DATABASE TESTDB;

  
# Java Backend Assignment 2025
## Tech Stack
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- SQL Server Express
- Hibernate
- Postman

## Configuration
Update application.properties with your database information:
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


# Google Places Search App

## Tech Stack
- React
- Redux Toolkit + Redux Thunk
- Tailwind CSS
- Google Maps & Places API
- Java Spring Boot
- SQL Server

## Features
- Google Places autocomplete
- Map display with marker
- Search history
- Favourite places
- Favourite sync with SQL Server
- Click history or favourites to re-center map

## How to Run
1. Start SQL Server
2. Run Spring Boot backend (Same with Java Backend Assignment 2025)
3. npm install(First Time)
4. npm start

## How to Use
1. Open the web app
2. Login/Signup
3. Search a place using the Google autocomplete box
4. Click on a result to see it on the map
5. Click ⭐ to save as favourite
6. Click ⭐ again to remove favourite
7. Able to click on favourites or history to search the place

## Architecture
React UI
↓
Redux Toolkit (store, thunk middleware)
↓
Spring Boot REST API
↓
SQL Server Database


## Author
Low Chi Haw
