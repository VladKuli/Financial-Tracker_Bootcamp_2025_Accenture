package org.financialTracker.controller;


import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.model.User;
import org.financialTracker.service.AuthService;
import org.financialTracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/get-all")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) throws AuthException {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted user with id " + id);
    }
}
