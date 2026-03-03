# requestsandresponses

DTOs for API requests and responses. All responses extend `BaseResponse` (HTTP status + message variables).

| Class | Sub-package | Description |
|---|---|---|
| `BaseResponse` | `base` | Abstract base — holds `httpStatusCode` and `List<MessageVariable>` |
| `MessageVariable` | `base` | Carries a response code string and a message string |
| `AuthRequest` | `auth` | Login/password-reset request — username and password fields |
| `AuthResponse` | `auth` | Extends `BaseResponse` — adds JWT `token` field |
| `UserResponse` | `user` | Extends `BaseResponse` — adds `User` object |
| `UserMetadataResponse` | `user` | Extends `BaseResponse` — adds list of `LiteRole` for role assignment |
| `ChangeUserPasswordRequest` | `user` | Carries userId and new password |
| `RoleResponse` | `role` | Extends `BaseResponse` — adds `Role` object |
| `RoleMetadataResponse` | `role` | Extends `BaseResponse` — adds `GenericPermissions` and `EntityPermissions` lists |
| `AuditResponse` | `audit` | Extends `BaseResponse` — adds `AuditDetails` object |
| `NavbarResponse` | `navbar` | Extends `BaseResponse` — adds list of `Navbar` objects |
| `GridRequest` | `grid` | Grid data request — entityId, page, size, sort criteria, filter criteria |
| `GridDataResponse` | `grid` | Extends `BaseResponse` — adds `gridData` list and total count |
| `GridMetadataResponse` | `grid` | Extends `BaseResponse` — adds column configs and filter configs |
| `SortCriteria` | `grid` | Holds sortColumnId and sort direction |
| `FilterCriteria` | `grid` | Holds filterColumnId, filterType, and filter value |
| `ExceptionResponse` | `exception` | Extends `BaseResponse` — used by the global exception handler |
