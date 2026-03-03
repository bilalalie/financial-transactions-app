package com.fintech.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /*
     * Converts your secret string into a cryptographic Key object.
     * HS256 requires a key of at least 256 bits (32 characters).
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


//     CREATION:Takes user details and wraps them into a signed JWT string.

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(); // You could add roles here if needed
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // The "sub" claim (who is this?)
                .setIssuedAt(new Date())                // When was it made?
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // When does it die?
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // The digital signature
                .compact(); // Turn into the final "header.payload.signature" string
    }

    /*
     * READ:
     * Decrypts/Parses the token to see who it belongs to.
     * This will throw an exception if the signature doesn't match the secret!
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /*
     * VERIFICATION:
     * Checks two things:
     * 1. Does the username in the token match the user we found in the DB?
     * 2. Is the token still within its "life" (not expired)?
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        Date expDate = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return username.equals(userDetails.getUsername()) && !expDate.before(new Date());
    }
}

