# resources

Application resources: database scripts, configuration, and email templates.

## sql/

PostgreSQL database initialization scripts.

| File | Description |
|---|---|
| `tables.sql` | Defines all 14 tables: users, user_verification_info, roles, role_permission_mapping, user_role_mapping, generic_permissions, entity_permissions, entities, audit_log, navbar, navbar_items, grid_column_config, filter_config, data_types |
| `sequences.sql` | Creates 6 auto-increment sequences (starting at 1024 to avoid collision with seed data) |
| `insert.sql` | Seeds permissions, admin role, admin user (admin/admin), navbars, data types, grid/filter configs |
| `select.sql` | Reference query for resolving a user's entity permissions through the RBAC hierarchy |
| `generate.json` | JSON schema reference for table structures (not executed) |

## templates/

FreeMarker (`.ftl`) email templates rendered by `EmailServiceImpl`.

| File | Description |
|---|---|
| `forgot-password-email.ftl` | Password reset email — variables: `firstName`, `link` |
| `user-creation-email.ftl` | Account creation email — variables: `firstName`, `link` |

## Configuration

Key `application.properties` settings: `spring.datasource.url/username/password` (PostgreSQL), `jwt.key` (HS256 signing key), `spring.mail.host/port/username/password` (SMTP).
