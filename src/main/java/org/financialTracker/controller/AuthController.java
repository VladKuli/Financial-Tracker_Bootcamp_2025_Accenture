package org.financialTracker.controller;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.JwtRequest;
import org.financialTracker.dto.JwtResponse;
import org.financialTracker.dto.RefreshJwtRequest;
import org.financialTracker.model.User;
import org.financialTracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) throws AuthException {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestParam RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = JwtUtil.getTokenFromRequest(request); // ✅ Используем утилитарный метод
        if (token != null) {
            authService.logout(token);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) throws AuthException {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
