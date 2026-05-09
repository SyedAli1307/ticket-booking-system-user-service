package com.ticketbooking.userservice.controller;

import com.ticketbooking.userservice.dto.ApiResponse;
import com.ticketbooking.userservice.dto.JwtResponse;
import com.ticketbooking.userservice.dto.LoginRequest;
import com.ticketbooking.userservice.dto.RegisterRequest;
import com.ticketbooking.userservice.dto.UserDto;
import com.ticketbooking.userservice.dto.UserUpdateRequest;
import com.ticketbooking.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        JwtResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        UserDto user = authService.getCurrentUser(authHeader);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User profile fetched successfully", user));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable("id") int id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserDto user = authService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User profile updated successfully", user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable("id") int id) {
        authService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "User deleted successfully", null));
    }
}