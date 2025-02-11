package org.financialTracker.security;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.financialTracker.model.JwtAuthentication;
import org.financialTracker.service.RevokedTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.financialTracker.util.JwtUtil;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtProvider;
    private final RevokedTokenService revokedTokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String token = JwtUtil.getTokenFromRequest(request);

            if (token != null) {
                if (revokedTokenService.isTokenRevoked(token)) {
                    log.warn("Access token is revoked. Blocking request.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is revoked");
                    return;
                }

                if (jwtProvider.validateAccessToken(token)) {
                    Claims claims = jwtProvider.getAccessClaims(token);
                    JwtAuthentication jwtInfoToken = JwtUtil.generate(claims);

                    jwtInfoToken.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
                }
            }
        } catch (JwtException e) {
            log.warn("JWT Error [{}]: {}", e.getClass().getSimpleName(), e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
            return;
        } catch (Exception e) {
            log.error("JWT processing error: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT processing error");
            return;
        }

        filterChain.doFilter(request, response);
    }


}

