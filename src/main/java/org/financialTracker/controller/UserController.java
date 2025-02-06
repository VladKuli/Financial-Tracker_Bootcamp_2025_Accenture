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

    // 2nd part
    @GetMapping("/all")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted user with id " + id);
    }

}
