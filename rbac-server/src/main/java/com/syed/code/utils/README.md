# utils

Startup utilities and entity-to-service mapping registries. Initialized on `ApplicationReadyEvent`.

| Class | Description |
|---|---|
| `ApplicationStartupUtil` | Listens for `ApplicationReadyEvent` and initializes all mapping utilities + JWT key loading |
| `EntityClassMapUtil` | Maps `EntityEnums.Entity` to Java entity classes (User, Role, AuditLog) |
| `EntityServiceMapUtils` | Maps `EntityEnums.Entity` to service classes (UserService, RoleService) and resolves beans |
| `EntityAuditDetailsServiceMapUtil` | Maps `EntityEnums.Entity` to audit detail service classes (Role-specific or generic fallback) |
| `EntityGridSanitizationMapUtil` | Maps `EntityEnums.Entity` to grid sanitization service classes (AuditLog-specific or generic fallback) |
