# services

Business logic layer. Each sub-package contains an interface and its `*Impl` class.

| Class | Sub-package | Description |
|---|---|---|
| `AuthService` / `AuthServiceImpl` | `auth` | Login, password reset, forgot password, account lockout after 5 failed attempts |
| `UserService` / `UserServiceImpl` | `user` | User CRUD, role assignment, sends creation email in a separate thread |
| `CustomUserDetailsService` / `CustomUserDetailsServiceImpl` | `user/details` | Spring Security `UserDetailsService` — loads user by username for authentication |
| `RoleService` / `RoleServiceImpl` | `role` | Role CRUD with privilege escalation prevention |
| `RoleAuditDetailsService` / `RoleAuditDetailsServiceImpl` | `role/audit` | Role-specific audit change comparison (handles permission mapping changes) |
| `JwtService` / `JwtServiceImpl` | `jwt` | JWT token generation and validation — HS256, 1-hour expiry |
| `LoggedInUserService` / `LoggedInUserServiceImpl` | `loggedinuser` | Retrieves current user from security context; central permission checking |
| `GridService` / `GridServiceImpl` | `grid` | Grid data retrieval — orchestrates query building, execution, and sanitization |
| `GridQueryService` / `GridQueryServiceImpl` | `grid/query` | Builds dynamic HQL queries with filtering, sorting, and pagination; also contains `QueryDetails` helper |
| `BaseGridSanitizationService` / `BaseGridSanitizationServiceImpl` | `grid/sanitization` | Abstract base for post-processing grid row data |
| `BaseVersioningGridSanitizationServiceImpl` | `grid/sanitization/versioning` | Abstract sanitizer for versioned entities — resolves createdBy/modifiedBy user names |
| `GenericVersioningGridSanitizationServiceImpl` | `grid/sanitization/versioning` | Default sanitizer for versioned entities (User, Role) |
| `BaseNonVersioningGridSanitizationServiceImpl` | `grid/sanitization/nonversioning` | Abstract sanitizer for non-versioned entities |
| `GenericNonVersioningGridSanitizationServiceImpl` | `grid/sanitization/nonversioning` | Default sanitizer for non-versioned entities |
| `AuditService` / `AuditServiceImpl` | `audit` | Creates audit log entries and retrieves audit details |
| `GenericAuditDetailsService` / `GenericAuditDetailsServiceImpl` | `audit/details` | Generic field-level change comparison using reflection |
| `AuditLogGridSanitizationService` / `AuditLogGridSanitizationServiceImpl` | `audit/grid` | Audit log grid sanitizer — resolves entity names, user names, status display names |
| `EmailService` / `EmailServiceImpl` | `email` | Renders FreeMarker templates and sends emails via Spring Mail |
| `BaseEntityService` / `BaseEntityServiceImpl` | `baseentity` | Core save pipeline — handles versioning, permission checks, uniqueness validation, and audit logging |
| `NavbarService` / `NavbarServiceImpl` | `navbar` | Fetches permission-filtered navigation menu for the logged-in user |
| `ObjectHashMapperServiceImpl` | `objecthashmapper` | Reflection-based entity-to-HashMap conversion for audit comparison |
