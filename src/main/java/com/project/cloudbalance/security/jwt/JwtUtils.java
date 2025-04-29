package com.project.cloudbalance.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "nESidFQEtK4VoZx6FCER/kHzp4J9MjNo4v2rgatqkJC=";
    private static final SecretKey secretkey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    private static final long expirationTime = 900000;

    public String generateToken(String username, String role){
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretkey)
                .compact();
    }

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public Date extractExpiration(String token)
    {

            return extractClaim(token, Claims::getExpiration);


    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        Claims claims = Jwts.parser()
                .verifyWith((secretkey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, String username)
    {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

}
