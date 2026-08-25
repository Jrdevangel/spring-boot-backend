Backend Module

Spring Boot backend module implementing secure authentication, authorization, JWT session management, and role-based access control (RBAC).

🚀 Current Features

The backend currently includes:

Stateless JWT-based authentication

Persistent refresh token management

Secure refresh token rotation strategy

Logout endpoint with refresh token revocation

Token replay/reuse protection

Centralized HTTP exception handling

DTO validation using Jakarta Validation

Route-level authorization with Spring Security

Method-level authorization using @PreAuthorize

Role-Based Access Control (RBAC)

Resource-based authorization (ABAC)

Swagger/OpenAPI integration

Refresh token persistence with PostgreSQL

Manual API verification using Postman

Spring Boot Actuator health endpoint

Profile-based configuration (dev / prod)

Environment variable secret externalization

Swagger disabled in production

Production-ready environment configuration

🧩 Module Structure

com.example.webserver

├── config
│   └── SecurityConfig.java
├── controller
│   ├── AuthController.java
│   ├── UserController.java
│   └── AdminController.java
├── dto
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── RefreshTokenRequest.java
│   ├── LogoutRequest.java
│   ├── AuthResponse.java
│   └── UserProfileResponse.java
├── entity
│   ├── UserEntity.java
│   ├── RefreshToken.java
│   └── Role.java
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── InvalidCredentialsException.java
│   ├── UserAlreadyExistException.java
│   ├── UserNotFoundException.java
│   ├── TokenExpiredException.java
│   └── TokenReuseException.java
├── repository
│   ├── UserRepository.java
│   └── RefreshTokenRepository.java
├── security
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── service
│   ├── AuthService.java
│   ├── UserService.java
│   └── RefreshTokenService.java
└── resources

🔐 Authentication & Authorization

JWT-Based Authentication

The system uses stateless authentication via JWT:

Access Tokens (short-lived)

Refresh Tokens (persistent and stored in PostgreSQL)

Authentication Flow

Register / Login
↓
Access Token + Refresh Token
│
├── Access Token expires
│          ↓
│   POST /api/auth/refresh
│          ↓
│   New Access Token + New Refresh Token
│
└── POST /api/auth/logout
↓
Refresh Token revoked
↓
Future refresh blocked

Refresh Token Strategy

Refresh tokens are persisted in PostgreSQL

Each refresh token has an expiration date

Tokens are rotated on every refresh request

Old refresh tokens are revoked

Logout invalidates refresh tokens

Reused, expired, or revoked refresh tokens trigger security exceptions

This design mitigates replay attacks and improves session security.

🛡 Role-Based Access Control (RBAC)

The backend implements RBAC using Spring Security authorities.

The authorization model combines route-level security with method-level authorization, both backed by Spring Security roles.

Available Roles

USER
ADMIN

Authority Mapping

Roles are automatically mapped to Spring Security authorities:

USER  → ROLE_USER
ADMIN → ROLE_ADMIN

via:

SimpleGrantedAuthority(
"ROLE_" + user.getRole().name()
)

Route-Level Authorization

Configured in:

SecurityConfig.java

Protected routes:

/api/user/**   → USER or ADMIN
/api/admin/**  → ADMIN only

Method-Level Authorization

Method security is enabled using:

@EnableMethodSecurity

Example:

@PreAuthorize("hasRole('ADMIN')")

🔐 Resource-Based Authorization (ABAC)

The backend also implements resource-based authorization for user resources.

The authorization rules are enforced through UserAuthorizationService.

Current policy:

USER  → Can access and modify their own resource
ADMIN → Can access and modify any user resource
ADMIN → Can delete user resources
ADMIN → Can manage user roles

This complements RBAC by considering the relationship between the authenticated principal and the requested resource.

🏗 Security Architecture

Request
│
▼
JwtAuthenticationFilter
│
▼
SecurityContext
│
▼
SecurityFilterChain
(Route authorization)
│
▼
@PreAuthorize
(Method authorization)
│
▼
UserAuthorizationService
(Resource authorization / ABAC)
│
▼
Controller

🔄 Authentication Endpoints

Method

Endpoint

Description

POST

/api/auth/register

Register new user

POST

/api/auth/login

Authenticate user

POST

/api/auth/refresh

Refresh access token

POST

/api/auth/logout

Revoke refresh token

🧠 Security Layer

JwtAuthenticationFilter

The request authentication layer:

Intercepts incoming requests

Extracts JWT from Authorization header

Validates JWT signature and expiration

Loads authenticated user

Injects user into Spring Security SecurityContext

Authorization format:

Authorization: Bearer <access_token>

Public endpoints:

/api/auth/register
/api/auth/login
/api/auth/refresh
/swagger-ui/** (development only)
/v3/api-docs/** (development only)
/actuator/health

All remaining endpoints require authentication.

Authentication is performed by JwtAuthenticationFilter, while authorization is enforced by Spring Security through request matchers, method-level security annotations, and resource-based authorization.

🗄 Database Configuration

The backend uses PostgreSQL with Spring Data JPA.

Current setup:

PostgreSQL database

Profile-based Hibernate configuration

ddl-auto=update (development)

ddl-auto=validate (production)

Automatic entity-to-table mapping

Environment variable support for credentials

Managed via:

application.yml

🐳 Docker Setup

Start infrastructure:

docker compose up -d

🐳 Docker Hardening

The Docker environment includes:

PostgreSQL healthcheck

Ordered startup using depends_on + service_healthy

Environment variable-based secrets

Persistent Docker volumes

Production profile activation

Health validation:

`pg_isready` -U ${DB_USERNAME} -d webserver_db

This guarantees PostgreSQL readiness before backend startup.

Services:

PostgreSQL database

Spring Boot backend

Frontend application

Caddy reverse proxy

⚠️ Local Development Warning

Do not run the Dockerized backend and local Spring Boot simultaneously.

Both use:

Port 8080

Running both causes:

Port 8080 was already in use

✅ API Verification

The backend has been manually verified using Postman.

Validated flows:

Register

Login

JWT authentication

Refresh token persistence

Refresh token rotation

Logout token revocation

RBAC (USER vs ADMIN)

Resource ownership authorization (ABAC)

Protected endpoints

JWT authorization via Bearer token

📈 Architectural Maturity

Authentication

JWT

Refresh Tokens

BCRYPT

Authorization

RBAC

Resource ownership authorization (ABAC)

Route-level authorization

Method-level authorization

Reliability

Global exception handling

DTO validation

Infrastructure

PostgreSQL

Docker

Profiles

Actuator

⚙️ Environment Profiles

The backend uses profile-based configuration.

Shared Configuration

application.yml

Contains common configuration:

JWT configuration

Shared JPA settings

open-in-view=false

Development Profile

application-dev.yml

Used for local development:

PostgreSQL local database

ddl-auto=update

SQL logs enabled

Run:

mvn spring-boot:run -Dspring-boot.run.profiles=dev

Production Profile

application-prod.yml

Used for Docker/production deployments:

Externalized datasource

Externalized JWT secret

ddl-auto=validate

SQL logging disabled

Swagger/OpenAPI disabled

SQL initialization disabled

Activated automatically in Docker:

SPRING_PROFILES_ACTIVE=prod

🔐 Environment Variables

Sensitive values are externalized and never committed to Git.

Required variables:

DB_URL=JDBC:postgresql://localhost:5432/webserver_db
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret

The repository includes:

.env.example

for secure environment setup.

📊 Observability

Only the health endpoint is publicly exposed.
All remaining Actuator endpoints require authentication or remain disabled.

Available endpoint:

/actuator/health

Used for:

Health monitoring

Infrastructure readiness checks

Container orchestration probes

Current exposure:

/actuator/health → public access

All remaining actuator endpoints remain restricted.