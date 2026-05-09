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

## Environment variables
| Variable        | Description                        | Example               |
|-----------------|------------------------------------|-----------------------|
| DB_USERNAME     | PostgreSQL username                | postgres              |
| DB_PASSWORD     | PostgreSQL password                | secret                |
| JWT_SECRET      | Base64-encoded 256-bit HMAC key    | (generated)           |
| JWT_EXPIRATION  | Access token TTL in ms             | 86400000              |

## Run locally
```bash
docker-compose up -d postgres
mvn spring-boot:run
```

## Deploy to Kubernetes
```bash
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/secret.yml
kubectl apply -f k8s/deployment.yml
kubectl apply -f k8s/service.yml
```
