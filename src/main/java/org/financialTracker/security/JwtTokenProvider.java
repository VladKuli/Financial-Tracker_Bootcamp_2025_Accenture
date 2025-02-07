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
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .expiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("roles", userDetails.getAuthorities())
                .claim("name", userDetails.getUsername())
                .compact();
    }

    /**
     * Generation of refresh token
     */
    public String generateRefreshToken(@NonNull UserDetails userDetails) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant  = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant );

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .expiration(refreshExpiration)
                .signWith(jwtAccessSecret)
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
