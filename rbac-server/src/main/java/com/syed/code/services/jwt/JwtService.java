package com.syed.code.services.jwt;

import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {

    public String extractJwtDetails(String jwt);

    public Claims extractAllClaims(String jwt);

    public String generateToken(Map<String, Object> extraClaims);

    public boolean isTokenValid(String jwt);
}
