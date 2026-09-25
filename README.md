# GTFS Polling Project
The goal of this project is to synchronize and store both real-time and static transit data based on the [General Transit Feed Specification](https://gtfs.org/) (GTFS & GTFS-RT)

## Overview
This application acts as a middle-tier data collector. It polls external transit apis periodically to fetch static transit data (like stops, routes, and trips) and real-time transit data (Vehicle positions, occupancy) then it stores it in a PostgreSQL database for usage by other applications.

## Key Features
* Automated Scheduling:
  * Static Transit Data: Synchronizes stops, routes, and trips daily - to account for transit route updates.
  * Real-time Transit Data: Fetches live vehicle positions and updates every 30 seconds.
* Data Persistence: Utilizes Spring Data JPA with Hibernate for storage and batch processing of API requests.
* API Security: Implements custom API Key authentication filters to secure REST API endpoints.
* Resilience: Configured with retry logic for reliable data fetching, independent of network.

## Technologies
* Java 17+
* Spring Boot (Web, JPA, Scheduling, Security)
* PostgreSQL
* Maven

## Configuration
You will first need a PostgreSQL database to have the data stored in, and a user with read/write access to that database.

The application requires these following environment variables. Ensure that they are stored as environment variables, or declared in a `.env` file.
|Variable|Description|
|--------|-----------|
|`SPRING_DATASOURCE_URL`|JDBC URL to the PostgreSQL database (localhost:8080)|
|`SPRING_DATASOURCE_USERNAME` |Username for the psql database|
|`SPRING_DATASOURCE_PASSWORD` |Password for the psql database|
|`POLLINGPROJECT_API_KEY`     |API key for accessing this app's endpoint|
|`GTFS_API_KEY`               |API key used to poll the external GTFS provider|
|`GTFS_API_URL`               |Base URL of the external GTFS provider|

The layout for these can be found in the `.env.example` file

## How to build and run
This project is created using the Maven Wrapper.
1. Build the project:
   - `./mvnw clean install`
2. Run the application:
   - `./mvnw sprint-boot:run`

## Roadmap
- [ ] Add webhook integration to keep front-end devices up to date with live information
- [ ] Clean up the API endpoints to provide the data cleaner (move static data into one controller)