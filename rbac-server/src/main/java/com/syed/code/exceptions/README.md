# exceptions

Custom exception classes.

| Class | Description |
|---|---|
| `PermissionMissingException` | Thrown when a user lacks permission for an action. Carries the `EntityEnums.Entity` involved. Handled by `CustomResponseEntityExceptionHandler` as HTTP 403. |
