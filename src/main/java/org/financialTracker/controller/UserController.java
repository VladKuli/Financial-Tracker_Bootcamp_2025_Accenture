package org.financialTracker.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.model.User;
import org.financialTracker.service.AuthService;
import org.financialTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;


    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUser() throws AuthException {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser( user));
    }

}
