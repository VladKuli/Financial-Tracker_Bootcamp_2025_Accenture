package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.mapper.UserMapper;
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
    public void createUser(User user) {
        userRepository.save(user);
    }
}
