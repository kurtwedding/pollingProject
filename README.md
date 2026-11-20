# GTFS Polling Project
The goal of this project is to synchronize and store both real-time and static transit data based on the [General Transit Feed Specification](https://gtfs.org/) (GTFS & GTFS-RT)

## Overview
This application acts as a middle-tier data collector. It polls external transit apis periodically to fetch static transit data (like stops, routes, and trips) and real-time transit data (Vehicle positions, occupancy) then it stores it in a PostgreSQL database for usage by other applications.

[![codecov](https://codecov.io/github/kurtwedding/pollingProject/graph/badge.svg?token=LK50ULPBC0)](https://codecov.io/github/kurtwedding/pollingProject)

## Key Features
* Automated Scheduling:
  * Static Transit Data: Synchronizes stops, routes, and trips daily - to account for transit route updates.
  * Real-time Transit Data: Fetches live vehicle positions and updates every 30 seconds.
* Data Persistence: Utilizes Spring Data JPA with Hibernate for storage and batch processing of API requests.
* API Security: Implements custom API Key authentication filters to secure REST API endpoints.
* Resilience: Configured with retry logic for reliable data fetching, independent of network.
* Automatic Deployment: This application automatically deploys to the aws cloud to ensure high-reliability.

## Technologies
* Java 25+
* Spring Boot (Web, JPA, Scheduling, Security)
* PostgreSQL
* Maven
* Terraform
* AWS Cloud (EC2, RDS, Secrets Manager, VPC)

## Configuration
You will first need to have valid aws credentials/token on the device you are launching the cloud from, using `aws login` or `aws sso login`

All of the environment variables for this app are deployed automatically by AWS, but terraform will prompt you for the following variables if they have not already been defined in a `terraform.tfvars` file.

|Variable|Description|
|--------|-----------|
|`gtfs_provider_api_key`               |API key used to poll the GTFS provider|
|`gtfs_provider_api_url`               |Base URL of the GTFS provider|
| `ssh_address` | CIDR block of addresses allowed to ssh into the EC2| 

You will also need to provision yourself a keypair for your ec2 instance called `gtfs-ssh-keypair.pem` and store it in the `./.ssh` directory.

## How to build and run
Once configured, you can run the app by running `deploy.sh` in the base project directory

## Roadmap
- [ ] Add webhook integration to keep front-end devices up to date with live information
- [ ] Clean up the API endpoints to provide the data cleaner (move static data into one controller)
