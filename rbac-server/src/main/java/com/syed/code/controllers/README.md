# controllers

REST API controllers under `/api/`. All use `@RestController`.

| Class | Base Path | Description |
|---|---|---|
| `AuthController` | `/api/auth` | Login, password reset, forgot password, verification flows |
| `UserController` | `/api/user` | Create user, get user, change password, user metadata |
| `RoleController` | `/api/role` | Create role, get role, role metadata |
| `GridController` | `/api/grid` | Dynamic grid data retrieval with filtering/sorting/pagination |
| `AuditController` | `/api/audit` | Get audit log details |
| `NavbarController` | `/api/navbar` | Permission-filtered navigation menu |
