package org.financialTracker.controller;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.model.User;
import org.financialTracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
        UserDTO createdUserDTO = UserMapper.toDTO(userService.createUser(user));
        return ResponseEntity.ok(createdUserDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        UserDTO updatedUserDTO = UserMapper.toDTO(userService.updateUser(id, user));
        return ResponseEntity.ok(updatedUserDTO);
    }

}
