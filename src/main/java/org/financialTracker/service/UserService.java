package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JpaUserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void createUser(User user) {
        userRepository.save(user);
    }
}
