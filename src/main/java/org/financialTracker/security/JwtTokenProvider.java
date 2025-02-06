package org.financialTracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtTokenProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret
    ) {
        this.jwtAccessSecret = generateKey(jwtAccessSecret);
        this.jwtRefreshSecret = generateKey(jwtRefreshSecret);
    }

    /**
     * Generating a SecretKey from a string
     */
    private SecretKey generateKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generating an access-token
     */
    public String generateAccessToken(@NonNull UserDetails userDetails) {
        return generateToken(userDetails, jwtAccessSecret, 30);
    }

    /**
     * Generation of refresh token
     */
    public String generateRefreshToken(@NonNull UserDetails userDetails) {
        return generateToken(userDetails, jwtRefreshSecret, 43200);
    }

    /**
     * The main method of token generation
     */
    private String generateToken(UserDetails userDetails, Key key, int expirationMinutes) {
        Instant now = Instant.now();
        Instant expirationInstant = now.plusSeconds(expirationMinutes * 60L);
        Date expiration = Date.from(expirationInstant);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .expiration(expiration)
                .issuedAt(Date.from(now))
                .signWith(key)
                .compact();
    }

    /**
     * Access token validation
     */
    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, jwtAccessSecret);
    }

    /**
     * Validation of refresh token
     */
    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, jwtRefreshSecret);
    }

    /**
     * General logic for token verification
     */
    private boolean validateToken(@NonNull String token, @NonNull SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Retrieving claims from an access token
     */
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    /**
     * Receiving claims from refresh token
     */
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    /**
     * The general method of obtaining claims
     */
    private Claims getClaims(@NonNull String token, @NonNull SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
