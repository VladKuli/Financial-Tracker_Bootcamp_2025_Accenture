package org.financialTracker.service;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.request.ChangePasswordDTO;
import org.financialTracker.dto.request.UpdateUserDTO;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.dto.request.CreateUserDTO;
import org.financialTracker.exception.UserNotFoundException;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final JpaUserRepository jpaUserRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getByUsername(String username) { return jpaUserRepository.findByUsername(username); }

    public Optional<User> getByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = jpaUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

    }

    // Get all users
    public List<UserResponseDTO> getUsers() {
        return UserMapper.toDTOList(
                jpaUserRepository.findAll()
        );
    }

    // Get user by entering id
    public UserResponseDTO getUser(long id) {
        return UserMapper.toDTO(
                jpaUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"))
        );
    }

    // Update current user
    public void updateUser(UpdateUserDTO updateUserDTO) throws AuthException {
        User currentUser = getByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (getByUsername(updateUserDTO.getUsername()).isPresent()) {
            throw new AuthException("Username is already taken");
        }
        if (getByEmail(updateUserDTO.getEmail()).isPresent()) {
            throw new AuthException("Email is already registered");
        }

        currentUser.setUsername(updateUserDTO.getUsername());
        currentUser.setName(updateUserDTO.getName());
        currentUser.setSurname(updateUserDTO.getSurname());
        currentUser.setEmail(updateUserDTO.getEmail());

        jpaUserRepository.save(currentUser);
    }

    public void deleteUser(Long id) {
        if (!jpaUserRepository.existsById(id)) {
            throw new UserNotFoundException("User with id '" + id + "' not found");
        }
        jpaUserRepository.deleteById(id);
    }

    public UserResponseDTO saveUser(CreateUserDTO createUserDTO){
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setName(createUserDTO.getName());
        user.setSurname(createUserDTO.getSurname());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(createUserDTO.getPassword());
        user.setRole(createUserDTO.getRole());
        return UserMapper.toDTO(jpaUserRepository.save(user));
    }

    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        User currentUser = getByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), currentUser.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword())) {
            throw new IllegalStateException("New password cannot match old password");
        }

        currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        jpaUserRepository.save(currentUser);
    }
}
