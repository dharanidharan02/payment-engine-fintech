# Payment Engine – FinTech Transaction Processing Service

A backend payment processing engine built with **Java**, **Spring Boot**, and **PostgreSQL** to simulate real-world fintech transaction workflows.  
This project demonstrates **payment validation**, **fraud screening**, **idempotent transaction creation**, and **structured REST API error handling**.

## Features

- Create and retrieve payment transactions using REST APIs
- Rule-based payment validation
- Fraud screening for suspicious transactions
- Idempotency handling to prevent duplicate transaction processing
- Conflict detection for reused idempotency keys with different payloads
- Structured exception handling with appropriate HTTP status codes
- PostgreSQL persistence for transaction and idempotency records

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **Maven**
- **Postman** for API testing

## Project Structure

```text
src/main/java/com/fintech/payment_engine
├── controller        # REST controllers
├── dto               # Request and response DTOs
├── exception         # Global exception handling and custom exceptions
├── model             # Entities and enums
├── repository        # JPA repositories
├── service           # Payment rules, fraud screening, orchestration
└── util              # Helper utilities
