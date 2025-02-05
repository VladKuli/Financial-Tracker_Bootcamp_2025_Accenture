package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

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
                userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"))
        );
    }

    // Create user
    public UserDTO createUser(User user) {
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            User updatedUser = userRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Expense with id '" + id + "' not found")
            );
            updatedUser.setUsername(user.getUsername());
            updatedUser.setName(user.getName());
            updatedUser.setSurname(user.getSurname());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setRole(user.getRole());
            userRepository.save(updatedUser);
            return UserMapper.toDTO(updatedUser);
        }
        // custom exception
        return null;
    }
}
