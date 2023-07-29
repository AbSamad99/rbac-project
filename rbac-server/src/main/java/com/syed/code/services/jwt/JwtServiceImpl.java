package com.syed.code.services.jwt;

import com.syed.code.entities.user.User;
import com.syed.code.enums.AuthEnums;
import com.syed.code.services.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private Environment environment;

    @Autowired
    private UserService userService;

    private static final String SECRET_KEY_PROPERTY_NAME = "jwt.key";
    private String SECRET_KEY_STRING;

    private Key SIGNING_KEY;

    public void init() {
        this.SECRET_KEY_STRING = environment.getProperty(SECRET_KEY_PROPERTY_NAME);
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY_STRING);
        this.SIGNING_KEY = Keys.hmacShaKeyFor(bytes);
    }

    public String extractJwtDetails(String jwt) {
        return jwt;
    }

    public Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String jwt) {
        Claims claims = extractAllClaims(jwt);
        Date expirationDate = claims.getExpiration();
        if (expirationDate.before(new Date(System.currentTimeMillis())) || claims.get("id") == null) return false;
        if (claims.get("username") == null) return false;
        String username = (String) claims.get("username");
        User user = userService.getVerificationLoadedUser(username);
        if (user == null || !user.getUserVerificationInfo().getStatus().equals(AuthEnums.UserStatus.Active.statusId))
            return false;

        return true;
    }
}
