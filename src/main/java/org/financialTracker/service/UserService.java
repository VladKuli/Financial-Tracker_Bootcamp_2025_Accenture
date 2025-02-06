package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.exception.UserNotFoundException;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.financialTracker.response.CoreError;
import org.financialTracker.response.UserResponse;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaUserRepository userRepository;

    // Get all users
    public List<UserDTO> getUsers() {
        return UserMapper.toDTOList(
                userRepository.findAll()
        );
    }

    // Get user by entering id
    public UserDTO getUser(long id) {
        return UserMapper.toDTO(
                userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"))
        );
    }

    // Create user
    public String createUser(User user) {
        userRepository.save(user);
        return new UserResponse("User created successfully").getMessage();
    }

    // Update user
    public User updateUser(Long id, UserDTO userDTO) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        updatedUser.setUsername(userDTO.getUsername()); // Assign passed body values
        updatedUser.setName(userDTO.getName());
        updatedUser.setSurname(userDTO.getSurname());
        updatedUser.setEmail(userDTO.getEmail());
        updatedUser.setPassword(userDTO.getPassword());
        updatedUser.setRole(userDTO.getRole());

        return userRepository.save(updatedUser);
    }
}
