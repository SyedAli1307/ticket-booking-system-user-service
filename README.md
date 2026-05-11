# user-service — ticket booking system

Microservice responsible for user registration, authentication, and JWT token management.
Part of the [Smart Ticket Booking System](../README.md) — a Kafka-driven microservices project
built with Spring Boot 3, Docker, and Kubernetes.

## Tech stack
- Java 17 · Spring Boot 3 · Spring Security 6
- PostgreSQL · Spring Data JPA
- JWT (jjwt 0.11) — access token 24h, refresh token 7d
- Docker · Kubernetes

## API endpoints
| Method | Endpoint                     | Auth     | Description          |
|--------|------------------------------|----------|----------------------|
| POST   | /api/v1/users/register       | Public   | Register new user    |
| POST   | /api/v1/users/login          | Public   | Login, get JWT       |
| GET    | /api/v1/users/me             | Bearer   | Get current user     |
| POST   | /api/v1/auth/refresh         | Bearer   | Refresh access token |
| POST   | /api/v1/auth/validate        | Bearer   | Validate token       |
