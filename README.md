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

src/main/java/com/fintech/payment_engine
├── controller        # REST controllers
├── dto               # Request and response DTOs
├── exception         # Global exception handling and custom exceptions
├── model             # Entities and enums
├── repository        # JPA repositories
├── service           # Payment rules, fraud screening, orchestration
└── util              # Helper utilities

## Core Business Logic

### Transaction Lifecycle

Each payment request is processed through a deterministic decision pipeline:

1. **Validation Layer**
2. **Fraud Screening Layer**
3. **Decision Assignment**
4. **Persistence Layer**
5. **Idempotency Handling**

---

### 1. Validation Rules (Deterministic Declines)

Transactions are immediately **DECLINED** if:

- Amount is null or ≤ 0
- Amount exceeds system threshold (e.g., > 10,000)
- Sender and receiver accounts are identical
- Currency is not supported (USD, CAD)
- Account format is invalid (`ACC + digits`)

---

### 2. Fraud Screening (Flagged Transactions)

Transactions are **FLAGGED** for manual review if:

- Amount exceeds high-risk threshold
- More than 3 transactions are initiated by the same sender within 60 seconds

---

### 3. Decision Engine

Final transaction state is determined as:

- `APPROVED` → Passed all checks
- `DECLINED` → Failed validation rules
- `FLAGGED` → Passed validation but failed fraud screening

---

### 4. Idempotency Handling

To ensure safe retry behavior in distributed systems:

- Same `Idempotency-Key` + identical request → returns original transaction
- Same `Idempotency-Key` + different request → returns **409 CONFLICT**

This prevents duplicate transaction creation in real-world payment systems.

---

### 5. Persistence

- Transactions are stored in PostgreSQL
- Idempotency records store:
  - request hash
  - idempotency key
  - transaction reference

## Architecture Overview

The system follows a layered microservice architecture:

- **Controller Layer** → Handles HTTP requests and responses
- **Service Layer** → Contains business logic, validation, and fraud rules
- **Repository Layer** → Manages database interactions using JPA
- **Model Layer** → Defines entities and enums for transaction domain
- Publishes payment transaction events to Kafka topic `payment-events` for asynchronous event-driven processing

Transaction flow:

Client → Controller → Service → Validation → Fraud Check → Decision → Database → Response
