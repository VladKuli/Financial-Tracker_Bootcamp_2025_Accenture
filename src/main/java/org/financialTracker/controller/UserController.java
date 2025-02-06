package org.financialTracker.controller;

import jakarta.security.auth.message.AuthException;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService){
        this.authService = authService;
    }


    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUser() throws AuthException {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }
}
