package org.financialTracker.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.request.UpdateUserDTO;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.model.User;
import org.financialTracker.service.AuthService;
import org.financialTracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;


    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getCurrentUser() throws AuthException {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

//    @PutMapping("/update")
//    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UpdateUserDTO updateUserDTO) throws AuthException {
//        return ResponseEntity.ok(userService.updateUser(updateUserDTO));
//    }

}
