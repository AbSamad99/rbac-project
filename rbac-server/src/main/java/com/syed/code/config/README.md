# config

Spring configuration classes for security, authentication, JPA, and email templating.

| Class | Description |
|---|---|
| `BCryptConf` | Provides a `PasswordEncoder` bean using BCrypt |
| `CustomSecurityConf` | Spring Security filter chain — defines public vs protected endpoints, stateless JWT session, CSRF disabled |
| `JwtFilterConf` | `OncePerRequestFilter` that extracts and validates the Bearer token on every request |
| `FreeMarkerConf` | Configures FreeMarker template engine for email rendering (loads from `classpath:/templates/`) |
| `SessionFactoryConf` | Configures JPA `EntityManagerFactory` and Hibernate `SessionFactory` beans |
