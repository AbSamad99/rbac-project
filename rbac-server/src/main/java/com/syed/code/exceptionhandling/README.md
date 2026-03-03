# exceptionhandling

Global exception handler using Spring's `@ControllerAdvice`.

| Class | Description |
|---|---|
| `CustomResponseEntityExceptionHandler` | Catches `PermissionMissingException` and returns HTTP 403 with an `ExceptionResponse` containing error code `E00_002`. |
