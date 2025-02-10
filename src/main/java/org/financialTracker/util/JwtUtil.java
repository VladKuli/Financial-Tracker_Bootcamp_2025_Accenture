package org.financialTracker.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.financialTracker.model.JwtAuthentication;
import org.financialTracker.model.Role;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtil {

    private static final String AUTHORIZATION = "Authorization";
    /**
     * Generates authentication details from JWT claims.
     */
    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtAuth = new JwtAuthentication();
        jwtAuth.setRoles(getRoles(claims));
        jwtAuth.setName(claims.get("name", String.class));
        jwtAuth.setUsername(claims.getSubject());
        return jwtAuth;
    }

    /**
     * Extracts roles from JWT claims and converts them into a Set of Role enums.
     */
    private static Set<Role> getRoles(Claims claims) {
        Object rolesObject = claims.get("roles"); // Extract the raw roles object

        if (!(rolesObject instanceof List<?> rawRoles)) {
            return Set.of();
        }

        return rawRoles.stream()
                .map(role -> {
                    if (role instanceof String) {
                        return (String) role; // If role is a plain string, use it directly
                    } else if (role instanceof LinkedHashMap) {
                        Object authority = ((LinkedHashMap<?, ?>) role).get("authority"); // Extract "authority" field
                        return authority instanceof String ? (String) authority : null;
                    }
                    return null; // Invalid role type
                })
                .filter(Objects::nonNull) // Remove null values
                .peek(role -> System.out.println(" Extracted role: " + role)) // Debug log
                .map(role -> role.replace("ROLE_", "")) // Remove "ROLE_" prefix if present
                .map(role -> {
                    try {
                        return Role.valueOf(role.toUpperCase()); // Convert to Enum, ensuring uppercase
                    } catch (IllegalArgumentException e) {
                        System.out.println(" Invalid role in token: " + role); // Log invalid role
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Remove null values (invalid roles)
                .collect(Collectors.toSet());

    }

    public static  String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
