# Spring Boot Backend

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=springboot\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven\&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Layered-orange)
![Status](https://img.shields.io/badge/Status-Active-success)

---

A Spring Boot backend providing REST API infrastructure, JWT authentication, refresh token rotation, role-based and resource-based authorization, PostgreSQL persistence, and Docker-based deployment.

## 🚀 Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* JWT authentication
* Refresh token rotation
* Spring Data JPA and Hibernate
* PostgreSQL
* Maven
* Docker and Docker Compose
* Caddy reverse proxy
* Nginx frontend

## 🏗 Architecture Overview

The backend follows a layered architecture:

**Controller → Service → Repository → Database**

Main components:

* REST API endpoints
* Stateless JWT authentication
* Persistent refresh token management and rotation
* Spring Security filter chain
* Route-level and method-level authorization
* Resource-based authorization (ABAC)
* PostgreSQL persistence
* Profile-based configuration
* Dockerized infrastructure
* Reverse proxy with Caddy

## 🖼 Infrastructure Overview

The Docker environment is designed around the following services:

* **PostgreSQL:** relational database.
* **Spring Boot Backend:** REST API and authentication services.
* **Nginx Frontend:** static frontend serving.
* **Caddy:** reverse proxy for HTTP/HTTPS traffic.
* **Docker network:** communication between containerized services.

The actual service configuration is defined in the Docker Compose configuration.

## 🔐 Authentication and Security

Implemented authentication features include:

* User registration and login
* JWT access token generation
* Refresh token generation and persistence
* Refresh token rotation
* Refresh token revocation on logout
* Protection against refresh token reuse
* BCrypt password hashing
* Spring Security integration
* Centralized exception handling
* Request validation

Authorization includes:

* Role-Based Access Control (RBAC)
* Route-level authorization
* Method-level authorization
* Resource-based authorization (ABAC)

## 🔄 Database and Environment Profiles

The application uses profile-based configuration.

### `dev` profile

Configured for local development:

* Connects to PostgreSQL.
* Uses the `webserver_db` database.
* Reads credentials from `DB_USERNAME` and `DB_PASSWORD`.
* Uses Spring Data JPA and Hibernate.
* Enables automatic schema updates with `ddl-auto: update`.
* Enables SQL logging.

### `prod` profile

Configured for production-oriented deployment:

* Uses externalized datasource configuration.
* Uses externalized credentials and JWT secrets.
* Uses `ddl-auto: validate`.
* Disables SQL logging.
* Disables Swagger/OpenAPI.
* Disables SQL initialization.

The production profile is configured for Docker deployment through `SPRING_PROFILES_ACTIVE=prod`, as documented in the backend module.

## 🧪 Running the Application

Run Maven commands from the `BackEnd/` directory, where the Maven project is located, using an available Maven installation or the IDE's Maven integration.

### Development Mode

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Profile

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

The production profile requires its corresponding environment variables and datasource configuration. For local development, use the `dev` profile.

## 🐳 Docker Setup

### 1. Configure environment variables

Create a `.env` file based on `.env.example`, if provided by the repository.

Example variable names:

```dotenv
POSTGRES_PASSWORD=your_secure_password
DB_URL=your_database_jdbc_url
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_base64_secret
```

The required variables and their values must match the application configuration and Docker Compose setup.

**Security:** Do not commit real credentials, database passwords, or JWT secrets to Git.

### 2. Start the environment

From the directory containing the applicable Compose file, run:

```bash
docker compose up --build
```

The configured environment includes the PostgreSQL database, Spring Boot backend, frontend, and Caddy reverse proxy.

### 3. Stop the environment

Stop and remove containers and networks without deleting persistent volumes:

```bash
docker compose down
```

**Warning:** `docker compose down -v` also removes declared volumes. This can permanently delete persisted database data. Use it only when you intentionally want to remove that data.

## 📌 Current Status

The backend documentation describes the following implemented capabilities:

* JWT authentication
* Persistent refresh tokens and token rotation
* Refresh token revocation and reuse protection
* Stateless Spring Security configuration
* RBAC and resource-based authorization
* PostgreSQL persistence
* Externalized configuration and secrets
* Docker-based infrastructure
* Caddy reverse proxy
* Spring Boot Actuator health endpoint
* Swagger/OpenAPI integration, disabled in production

## 🔜 Next Improvements

* HTTPS production deployment
* CI/CD pipeline
* Unit and integration testing
* Monitoring and observability

---

This project demonstrates the development of a Spring Boot backend with secure authentication, authorization, relational persistence, and containerized infrastructure.