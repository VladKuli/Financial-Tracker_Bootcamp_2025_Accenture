package org.financialTracker.service;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.financialTracker.dto.JwtRequest;
import org.financialTracker.dto.JwtResponse;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.dto.request.CreateUserDTO;
import org.financialTracker.mapper.UserMapper;
import org.financialTracker.model.Role;
import org.financialTracker.model.User;
import org.financialTracker.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RevokedTokenService revokedTokenService;
    private final ConcurrentHashMap<String, String> refreshStorage = new ConcurrentHashMap<>();

    /**
     * Register a new user
     */

    public UserResponseDTO registerUser(CreateUserDTO createUserDTO) throws AuthException {
        if (userService.getByUsername(createUserDTO.getUsername()).isPresent()) {
            throw new AuthException("Username is already taken");
        }
        if (userService.getByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new AuthException("Email is already registered");
        }
        if (!isValidPassword(createUserDTO.getPassword())) {
            throw new AuthException("Password does not meet security requirements");
        }

        //Set default role if not provided
        if (createUserDTO.getRole() == null) {
            createUserDTO.setRole(Role.USER);
        }

        //Hash the password before saving
        createUserDTO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        return userService.saveUser(createUserDTO);
    }

    /**
     * Authenticate user and generate tokens
     */
    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {

        UserDetails userDetails = userService.getByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AuthException("User not found"));

        // Check if the password matches the hashed password in the database
        if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
            throw new AuthException("Invalid password");
        }


        // Authenticate user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        // Generate JWT tokens
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        // Store refresh token
        refreshStorage.put(userDetails.getUsername(), refreshToken);

        // Return JWT response (UserDTO is not included for security reasons)
        return new JwtResponse(accessToken, refreshToken);
    }

    /**
     * Getting new access-token on refresh-token
     */
    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException{
        // Validate the refresh token
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException("Invalid refresh token");
        }

        // Extract claims from the refresh token
        Claims claims = jwtTokenProvider.getRefreshClaims(refreshToken);
        String username = claims.get("username", String.class);

        // Check if the refresh token exists in storage
        String storedRefreshToken = refreshStorage.get(username);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new AuthException("Refresh token mismatch or expired");
        }

        // Retrieve user from database
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));

        // Generate a new access token
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);

        // Return only the new access token (refresh token remains the same)
        return new JwtResponse(newAccessToken, null);
    }

    /**
     *  Refresh access and refresh tokens securely
     */
    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException{
        // Validate the refresh token before processing
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new AuthException("Invalid refresh token");
        }

        // Extract username from the refresh token claims
        Claims claims = jwtTokenProvider.getRefreshClaims(refreshToken);
        String username = claims.get("username", String.class);

        // Ensure the refresh token exists and matches the stored token
        String storedRefreshToken = refreshStorage.get(username);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new AuthException("Refresh token mismatch or expired");
        }

        // Retrieve the user from the database
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new AuthException("User not found"));

        // Generate new access and refresh tokens
        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        // Update refresh token in storage
        refreshStorage.put(username, newRefreshToken);

        // Return the new tokens
        return new JwtResponse(newAccessToken, newRefreshToken);
    }

    /**
     * Deleting refresh token
     */
    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required for logout");
        }

        Claims claims = jwtTokenProvider.getAccessClaims(token);
        String username = claims.getSubject();

        refreshStorage.remove(username);

        revokedTokenService.revokeToken(token);
    }

    /**
     * Obtaining information about an authenticated user
     */
    public UserResponseDTO getAuthenticatedUser() throws AuthException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.getByUsername(principal.toString())
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new AuthException("User not found"));

    }

    /**
     * Checking password complexity
     */
    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%^&+=!].*");
    }
}
