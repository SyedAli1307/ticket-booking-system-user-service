package com.ticketbooking.userservice.service;

import com.ticketbooking.userservice.dto.JwtResponse;
import com.ticketbooking.userservice.dto.LoginRequest;
import com.ticketbooking.userservice.dto.RegisterRequest;
import com.ticketbooking.userservice.dto.UserDto;
import com.ticketbooking.userservice.model.TokenModel;
import com.ticketbooking.userservice.model.UserModel;
import com.ticketbooking.userservice.repository.TokenRepository;
import com.ticketbooking.userservice.repository.UserRepository;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenRepository tokenRepository;

    public JwtResponse register(RegisterRequest request) {
        System.out.println("Registering user with email: " + request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("Registration failed: Email " + request.getEmail() + " already registered");
            throw new RuntimeException("Email already registered");
        }
        UserModel user = new UserModel();
        user.setName(request.getName());
        user.setUserName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        user = userRepository.save(user);

        String accessToken  = jwtService.generateToken(user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole());
        
        saveUserToken(user, accessToken, refreshToken);
        
        return buildResponse(accessToken, refreshToken, user.getEmail());
    }

    public JwtResponse login(LoginRequest request) {
        System.out.println("Attempting login for user: " + request.getEmail());

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken  = jwtService.generateToken(user.getEmail(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole());
        
        saveUserToken(user, accessToken, refreshToken);
        
        return buildResponse(accessToken, refreshToken, user.getEmail());
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new com.ticketbooking.userservice.exception.UnauthorizedException("Refresh token is invalid or expired");
        }
        String email = jwtService.extractUsername(refreshToken);
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.ticketbooking.userservice.exception.ResourceNotFoundException("User not found"));

        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getRole());
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole());

        saveUserToken(user, newAccessToken, newRefreshToken);

        return buildResponse(newAccessToken, newRefreshToken, user.getEmail());
    }

    private void saveUserToken(UserModel user, String accessToken, String refreshToken) {
        if (user == null) {
            throw new RuntimeException("Cannot save token for null user");
        }
        TokenModel token = new TokenModel();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        
        tokenRepository.save(token);
    }

    public UserDto getCurrentUser(String authHeader) {
        String token    = authHeader.replace("Bearer ", "");
        String email    = jwtService.extractUsername(token);
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.ticketbooking.userservice.exception.ResourceNotFoundException("User not found"));
        return mapToDto(user);
    }

    public UserDto updateUser(int id, com.ticketbooking.userservice.dto.UserUpdateRequest request) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new com.ticketbooking.userservice.exception.ResourceNotFoundException("User not found with id: " + id));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getUserName() != null) user.setUserName(request.getUserName());
        if (request.getAge() != null) user.setAge(request.getAge());
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new com.ticketbooking.userservice.exception.DuplicateResourceException("Email already taken: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        user = userRepository.save(user);
        return mapToDto(user);
    }

    public void deleteUser(int id) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new com.ticketbooking.userservice.exception.ResourceNotFoundException("User not found with id: " + id));
        
        // Delete all tokens for this user first
        tokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    private UserDto mapToDto(UserModel user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        dto.setAge(String.valueOf(user.getAge()));
        if (user.getCreatedAt() != null) {
            dto.setTimeStamp(user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        return dto;
    }

    private JwtResponse buildResponse(String access, String refresh, String email) {
        return new JwtResponse(access, refresh, "Bearer", email);
    }
}
