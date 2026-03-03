# repositories

Spring Data JPA repository interfaces. All extend `JpaRepository` and use JPQL `@Query` annotations.

| Class | Description |
|---|---|
| `UserRepository` | CRUD and lookup for `User` entities — by ID, key, username; also fetches `UserRoleMapping` entries |
| `RoleRepository` | CRUD for `Role`/`LiteRole`; fetches `RolePermissionMapping`, `GenericPermissions`, `EntityPermissions`; core RBAC permission resolution query |
| `UserVerificationInfoRepository` | Lookup `UserVerificationInfo` by user key, password reset code, or verification code |
| `AuditLogRepository` | Fetch `AuditLog` by ID |
| `FilterAndSortRepository` | Fetch and validate `GridColumnConfig` and `FilterConfig` entries by entity ID |
| `NavbarRepository` | Multi-join query returning `Navbar`/`NavbarItem` pairs filtered by user permissions |
