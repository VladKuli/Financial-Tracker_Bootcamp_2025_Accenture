package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<UserDTO> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserDTO> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO saveUser(User user) {

        if (userRepository.existsByUsername(UserMapper.toDTO(user).getUsername())) {
            throw new RuntimeException("A user with this username already exists");
        }

        if (userRepository.existsByEmail(UserMapper.toDTO(user).getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Хешируем пароль перед сохранением

        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

}
