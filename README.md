# RBAC Server

A Role-Based Access Control (RBAC) backend built with Spring Boot that provides JWT authentication, granular permission management, user/role administration, audit logging, and configurable data grids.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 3.0.6 | Application framework |
| Spring Security | 6.1.0 | Authentication & authorization |
| Spring Data JPA | 3.0.x | Data access (Hibernate ORM) |
| PostgreSQL | - | Database |
| JWT (jjwt) | 0.11.5 | Token-based authentication |
| Gradle | - | Build tool |
| Lombok | - | Boilerplate reduction |
| FreeMarker | 2.3.32 | Email templates |
| Spring Mail | 3.1.1 | Email sending |

## Project Structure

```
rbac-server/src/main/
├── java/com/syed/
│   ├── rbacserver/             # Application entry point
│   └── code/
│       ├── config/             # Security, JWT filter, BCrypt, session, email config
│       ├── controllers/        # REST API endpoints
│       ├── entities/           # JPA entities
│       │   ├── baseentity/     # Abstract base entities (versioning/non-versioning)
│       │   ├── user/           # User, CustomUserDetails, UserVerificationInfo
│       │   ├── role/           # Role, permissions, mappings
│       │   ├── audit/          # AuditLog, AuditDetails
│       │   ├── navbar/         # Navigation menu entities
│       │   ├── filterandSort/  # Grid column and filter config
│       │   └── email/          # Email DTO
│       ├── enums/              # Entity types, permissions, statuses, error/success codes
│       ├── exceptions/         # Custom exceptions
│       ├── exceptionhandling/  # Global exception handler
│       ├── repositories/       # JPA repositories
│       ├── requestsandresponses/ # Request/response DTOs
│       ├── services/           # Business logic layer
│       └── utils/              # Startup utilities and entity-service mappers
└── resources/
    ├── sql/                    # Database schema, sequences, seed data
    └── templates/              # FreeMarker email templates
```

## Package Documentation

| Package | Description |
|---|---|
| [rbacserver](rbac-server/src/main/java/com/syed/rbacserver/README.md) | Application entry point |
| [config](rbac-server/src/main/java/com/syed/code/config/README.md) | Security, JWT filter, BCrypt, session factory, FreeMarker |
| [controllers](rbac-server/src/main/java/com/syed/code/controllers/README.md) | REST API controllers |
| [entities](rbac-server/src/main/java/com/syed/code/entities/README.md) | JPA entities (users, roles, permissions, audit, navbar, grid config) |
| [enums](rbac-server/src/main/java/com/syed/code/enums/README.md) | Entity types, permissions, statuses, error/success codes |
| [exceptions](rbac-server/src/main/java/com/syed/code/exceptions/README.md) | Custom exception classes |
| [exceptionhandling](rbac-server/src/main/java/com/syed/code/exceptionhandling/README.md) | Global `@ControllerAdvice` exception handler |
| [repositories](rbac-server/src/main/java/com/syed/code/repositories/README.md) | Spring Data JPA repositories |
| [requestsandresponses](rbac-server/src/main/java/com/syed/code/requestsandresponses/README.md) | Request/response DTOs |
| [services](rbac-server/src/main/java/com/syed/code/services/README.md) | Business logic layer (auth, user, role, grid, audit, email, etc.) |
| [utils](rbac-server/src/main/java/com/syed/code/utils/README.md) | Startup utilities and entity-service mapping registries |
| [resources](rbac-server/src/main/resources/README.md) | SQL scripts, email templates, configuration |

## API Endpoints

### Authentication (`/api/auth`)

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/login` | No | Authenticate and receive JWT token |
| POST | `/reset-password` | Yes | Reset password for authenticated user |
| POST | `/forgot-password` | No | Request password reset email |
| POST | `/forgot-password-reset/{resetCode}` | No | Reset password using emailed code |
| GET | `/verify-password-reset-code/{resetCode}` | No | Verify reset code validity |

### Users (`/api/user`)

| Method | Path | Auth | Permission | Description |
|---|---|---|---|---|
| POST | `/create-user/` | Yes | Create User | Create a new user |
| POST | `/change-user-password/` | Yes | Change Password | Change a user's password |
| GET | `/get-user/{id}` | Yes | View User | Retrieve user by ID |
| GET | `/get-user-metadata` | Yes | - | Get available roles for user form |

### Roles (`/api/role`)

| Method | Path | Auth | Permission | Description |
|---|---|---|---|---|
| POST | `/create-role/` | Yes | Create Role | Create a new role |
| GET | `/get-role/{id}` | Yes | View Role | Retrieve role by ID |
| GET | `/get-role-metadata` | Yes | - | Get available permissions for role form |

### Grid (`/api/grid`)

| Method | Path | Auth | Permission | Description |
|---|---|---|---|---|
| POST | `/get-grid-data` | Yes | View (entity) | Fetch paginated, filtered, sorted data |
| GET | `/get-grid-metadata/{id}` | Yes | - | Get column and filter config for entity |

### Audit (`/api/audit`)

| Method | Path | Auth | Permission | Description |
|---|---|---|---|---|
| GET | `/get-audit-details/{id}` | Yes | View Audit Log | Retrieve audit log details |

### Navbar (`/api/navbar`)

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/get-navbar-info` | Yes | Get navigation menu filtered by user permissions |

## Authentication Flow

```
1. Client sends POST /api/auth/login with { username, password }
2. Server validates credentials via Spring Security AuthenticationManager
3. Server checks account status (Active, Locked, Disabled)
4. On success: JWT token generated (HS256, 1-hour expiry) and returned
5. Client includes token in subsequent requests: Authorization: Bearer <token>
6. JwtFilterConf intercepts each request, validates token, sets SecurityContext
```

- Account locks after **5 failed login attempts**
- Lock count resets on successful login
- Password reset codes expire after **48 hours**

## Permission Model

```
User ──> UserRoleMapping ──> Role ──> RolePermissionMapping ──> EntityPermissions
                                                                   ├── Entity (User/Role/AuditLog)
                                                                   └── GenericPermission (Create/View/Edit/Delete/Copy/ChangePassword)
```

Every protected operation calls `LoggedInUserService.isActionAllowed(entity, permission)` which traverses this hierarchy to determine access.

### Default Permissions

| Entity | Available Permissions |
|---|---|
| User | Create, View, Edit, Delete, Copy, Change Password |
| Role | Create, View, Edit, Delete, Copy |
| Audit Log | View |

## Database Schema

Key tables: `users`, `user_verification_info`, `roles`, `user_role_mapping`, `role_permission_mapping`, `entity_permissions`, `generic_permissions`, `entities`, `audit_log`, `navbar`, `navbar_items`, `grid_column_config`, `filter_config`, `data_types`.

All sequences start at **1024** to avoid collision with seed data.

See [`resources/sql/`](rbac-server/src/main/resources/sql/) for full DDL, sequences, and seed data.

## Setup & Running

### Prerequisites
- Java 17+
- PostgreSQL database
- Gradle

### Configuration
Set the following properties (via `application.properties` or environment variables):
- Database connection (URL, username, password)
- `jwt.key` — Base64-encoded secret key for JWT signing
- Mail server settings (host, port, username, password) for email features

### Build & Run
```bash
cd rbac-server
./gradlew bootRun
```

### Default Credentials
- **Username**: `admin`
- **Password**: `admin`
- **Role**: `admin` (has all permissions)

## Architecture

```
HTTP Request
  → JwtFilterConf (token validation)
  → SecurityContext (authenticated user)
  → Controller (receives request)
  → Service (business logic + permission check)
  → LoggedInUserService.isActionAllowed() (RBAC check)
  → Repository (database access via JPA/Hibernate)
  → Response DTO (BaseResponse with statusCode + messageVariables)
  → HTTP Response
```

The application follows a **Controller → Service → Repository** layered architecture with:
- Centralized exception handling via `@ControllerAdvice`
- Entity versioning (active/inactive records with version numbers)
- Audit logging on all entity operations
- Consistent response format via `BaseResponse`
