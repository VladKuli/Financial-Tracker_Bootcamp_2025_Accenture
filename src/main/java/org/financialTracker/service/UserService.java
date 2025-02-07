package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.UserDTO;
import org.financialTracker.exception.UserNotFoundException;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

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


    // Update user
    public UserDTO updateUser(User user) {
        User updatedUser = userRepository.findByUsername(user.getUsername())
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
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO saveUser(User user){
        return UserMapper.toDTO(userRepository.save(user));
    }
}
