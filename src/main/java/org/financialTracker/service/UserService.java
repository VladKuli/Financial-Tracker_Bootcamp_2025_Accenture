package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.exception.UserNotFoundException;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaUserRepository userRepository;
    private final JpaUserRepository jpaUserRepository;

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

    public User getCurrentUser(Authentication authentication) {
        String username = "coolUser";
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // TEST
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username);
    }

    // Create user
    public UserDTO createUser(User user) {
        User savedUser = jpaUserRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    // Update user
    public UserDTO updateUser(Long id, User user) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        updatedUser.setUsername(user.getUsername()); // Assign passed body values
        updatedUser.setName(user.getName());
        updatedUser.setSurname(user.getSurname());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
        userRepository.save(updatedUser);

        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!jpaUserRepository.existsById(id)) {
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        jpaUserRepository.deleteById(id);
    }
}
