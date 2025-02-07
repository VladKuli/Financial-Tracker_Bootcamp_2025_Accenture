package org.financialTracker.service;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.request.UpdateUserDTO;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.dto.request.CreateUserDTO;
import org.financialTracker.exception.UserNotFoundException;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final JpaUserRepository userRepository;

    public Optional<User> getByUsername(String username) { return userRepository.findByUsername(username); }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

    }

    // 2nd part
    // Get all users
    public List<UserResponseDTO> getUsers() {
        return UserMapper.toDTOList(
                userRepository.findAll()
        );
    }

    // Get user by entering id
    public UserResponseDTO getUser(long id) {
        return UserMapper.toDTO(
                userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"))
        );
    }

    // Update user
//    public UserResponseDTO updateUser(UpdateUserDTO updateUserDTO) throws AuthException {
//        User currentUser = authService.getAuthenticatedUserWoDTO();
//
//        currentUser.setUsername(updateUserDTO.getUsername()); // Assign passed body values
//        currentUser.setName(updateUserDTO.getName());
//        currentUser.setSurname(updateUserDTO.getSurname());
//        currentUser.setEmail(updateUserDTO.getEmail());
//        currentUser.setPassword(updateUserDTO.getPassword());
//        userRepository.save(currentUser);
//
//        return UserMapper.toDTO(currentUser);
//    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO saveUser(CreateUserDTO createUserDTO){
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setName(createUserDTO.getName());
        user.setSurname(createUserDTO.getSurname());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());
        user.setRole(createUserDTO.getRole());
        return UserMapper.toDTO(userRepository.save(user));
    }
}
