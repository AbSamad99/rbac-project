# entities

JPA entity classes representing database tables. Organized into sub-packages by domain.

| Class | Sub-package | Description |
|---|---|---|
| `BaseEntity<T>` | `baseentity` | Abstract base for versioned entities — provides key, audit fields, version, isActive |
| `NonVersioningEntity<T>` | `baseentity` | Abstract base for non-versioned entities — provides key and audit fields |
| `User` | `user` | User account (extends `BaseEntity`) — maps to `users` table |
| `CustomUserDetails` | `user` | Spring Security `UserDetails` implementation wrapping a `User` |
| `UserVerificationInfo` | `user` | Password hash, reset codes, lock count, account status — maps to `user_verification_info` table |
| `Role` | `role` | Role definition (extends `BaseEntity`) — maps to `roles` table |
| `LiteRole` | `role` | Lightweight role projection (id, key, name only) |
| `UserRoleMapping` | `role` | Many-to-many junction between users and roles |
| `RolePermissionMapping` | `role` | Maps a role to an entity permission with allow/deny flag (extends `NonVersioningEntity`) |
| `EntityPermissions` | `role` | Entity-specific permission linking a generic permission to an entity type |
| `GenericPermissions` | `role` | Base permission types (Create, View, Edit, Delete, Copy, Change Password) |
| `AuditLog` | `audit` | Action audit trail (extends `NonVersioningEntity`) — maps to `audit_log` table |
| `AuditDetails` | `audit` | DTO for audit change details — holds editDetails map with `"previousValue@@newValue"` format |
| `Navbar` | `navbar` | Navigation menu group |
| `NavbarItem` | `navbar` | Navigation menu item linked to an entity permission |
| `BaseConfig` | `filterandSort` | Abstract `@MappedSuperclass` for grid/filter configs — holds columnName, displayName, entityId |
| `GridColumnConfig` | `filterandSort` | Grid column definition per entity |
| `FilterConfig` | `filterandSort` | Filterable column definition per entity |
| `Email` | `email` | Email message DTO — templateName, to, from, subject, contentMap |
