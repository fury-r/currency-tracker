# Backend Engineer Assignment
In this assignment, you are expected to an existing project for an imaginary mobile application. Please read the constraints and tasks carefully and follow submission guideline at the bottom of the page.

## Scenario
BayzTracker is a cryptocurrency tracker app which allows its users to create alerts to be notified when a price of a coin reaches the price user determines.

User can create multiple alerts and can track the alert status anytime (triggered or waiting).

There is also currency list page where all coins with their current prices are listed.

## Tech stack
- [Spring Boot](https://spring.io/projects/spring-boot)
- Java 8+ or Kotlin
- Gradle or Maven
- Spring Data JPA
- Hibernate
- PostgreSQL
- GIT for version control

## General Application Constraints
- Users are using BayzTracker mobile app, assume that the API is only consumed by mobile
- Data should only be accepted from the registered users with their ownership rights.
- You don't have to implement a registration flow, you can add predefined users to database and use them in this project.
- You don't have to implement currency management flow, you can add predefined currencies to database.
- You don't have to secure endpoints

## Tasks
1. Implement API endpoints for maintaining the CRUD operations for alerts.
    - Alert Entity: `currency`, `targetPrice`, `createdAt`, `status(NEW, TRIGGERED, ACKED, CANCELLED)`
    - The status of the alert
        - NEW if the price is not in the target price
        - TRIGGERRED if the price is reached
        - ACKED if the user closes the alert
        - CANCELLED if the user cancels the alert
    - User can create/edit/delete the alerts
    - User can cancel the alert if it is not triggered yet
    - User can acknowledge the alert when he is notified.(The target price was reached)
2. Create a ScheduleTask that checks the alerts and notifies the users if the target price is reached
    - For the notification part you can write a simple log on console. Email or push notification is not considered here.
    - ScheduledTask should run every 30 seconds. 
    - Note that, we will change the current price information manually on database while testing.

## To run existing project with Docker
1. Application is already dockerized. You can run following command in root folder for setting up Postgres database. 
  - ```docker compose up```
2. Run application from Gradle or ide.

## Evaluation Criterias
- Code quality
- Applying Best Practices and OOP principles
- Correctness of the business logic and their compliance with the requirements
- Unit Tests
- Git commit structure

# Submission
Please compress everything you have prepared into a zip file & send it over email as attached to your Bayzat recruiter.

- Make sure that project is building correctly.
- Make sure that all tests are passing.
- Prepare necessary instructions to run your application in `DOC.md` file.
- If you have questions, please send email us, we'll get back to you as soon as possible.
