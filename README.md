# Auth Service

## Overview
The **Auth Service** provides authentication and authorization for the system.  
It is responsible for:
- Create User 
- User login with email and password
- Password hashing and validation
- JWT token generation and validation
- Block user

This service is built following **Clean Architecture** principles to ensure testability, maintainability, and scalability.

---

## Tech Stack
- **Java 21**
- **Spring Boot 3.5**
- **Spring WebFlux** (Reactive stack)
- **Spring Security** (Reactive)
- **JWT** (Generate Token)
- **Gradle** (build tool)
- **Docker** (containerization)
- **cleanArchitectureVersion** (Clean Architecture Bancolombia's Open Source Tool) 

---

## Architecture
- **Domain Layer**: Business rules (entities, value objects, contracts)
- **Application Layer**: Use cases (login, token validation, user management)
- **Infrastructure Layer**: 
  - **Driven Adapters**:
    - Spring Security configuration
    - JWT token provider
    - Password hashing (BCrypt)
    - Persistence adapters (R2DB)
  - **Entry Point**:
    - reactive-web (webflux)

---

## 🚀 Getting Started

### Prerequisites
- JDK 21+
- Docker (optional, for containerized environment)

