# enums

Application-wide enumerations. Each enum provides a static lookup map for ID-based retrieval.

| Class | Sub-package | Description |
|---|---|---|
| `AuditEnums` | — | Contains `AttemptStatus` enum: Success, FailedError, FailedMissingPerms, FailedBadRequest |
| `AuthEnums` | — | Contains `UserStatus` enum: Active, Active_Verification_Pending, Admin_Disabled, Deleted, Locked |
| `EntityEnums` | — | Contains `Entity` enum (User, Role, AuditLog) and `DataType` enum (Integer, String, Boolean, Date, Long) |
| `PermissionEnums` | — | Contains `GenericPerms` enum: Create, View, Edit, Delete, Copy, ChangePassword |
| `ErrorCodes` | `codes` | Error response codes (E00_001 through E00_010) with nested enums per scope |
| `SuccessCodes` | `codes` | Success response codes (S00_001 through S00_008) with nested enums per scope |
