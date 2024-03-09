# Candidate Management Service

This service allows you to manage candidates, directions, and tests. It is built using Java Spring Boot,
PostgreSQL database, FlyWay for Data Base migration, Log4j2 for logging, Minio for file storage and Swagger for API
documentation.

## Setup

1. Clone the repository.
2. Make sure you have Docker and Maven installed on your machine.
3. Run mvn clean install to build the project.
4. Run docker-compose up to start the service.
5. Access the service at http://localhost:8085.
6. Postman collection provides you test data for API testing, you can find it in te project root

## API Endpoints

- **Areas**
    - GET /areas: Get a list of directions.
    - POST /areas: Add a new direction.
    - PUT /areas/{id}: Update a direction.

- **Tests**
    - GET /tests: Get a list of tests.
    - POST /tests: Add a new test.
    - PUT /tests/{id}: Update a test.

- **Candidates**
    - GET /candidates: Get a list of candidates.
    - POST /candidates: Add a new candidate.
    - PUT /candidates/{id}: Update a candidate.

- **Candidate Tests**
    - GET /candidate_tests: Get a list of candidate tests.
    - POST /candidate_-tests: Add a new test for a candidate.
    - PUT /candidate_tests/{id}: Update a test for a candidate.

## Database Migration

The service includes database migration scripts to set up the initial database schema. These scripts are executed
automatically when the service starts.

## Swagger Documentation

Swagger documentation for the API endpoints can be accessed at http://localhost:8085/swagger-ui.html.

Feel free to explore the service and manage candidates, directions, and tests efficiently!