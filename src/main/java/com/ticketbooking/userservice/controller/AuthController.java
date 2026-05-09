package com.ticketbooking.userservice.controller;

import com.ticketbooking.userservice.dto.ApiResponse;
import com.ticketbooking.userservice.dto.JwtResponse;
import com.ticketbooking.userservice.service.AuthService;
import com.ticketbooking.userservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new com.ticketbooking.userservice.exception.BadRequestException("Authorization header is missing or invalid");
        }
        String token = authHeader.replace("Bearer ", "");
        JwtResponse refreshed = authService.refreshToken(token);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Token refreshed successfully", refreshed));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validate(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new com.ticketbooking.userservice.exception.BadRequestException("Authorization header is missing or invalid");
        }
        String token = authHeader.replace("Bearer ", "");
        boolean isValid = jwtService.isTokenValid(token);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK.value(), "Token validation result", isValid));
    }
}
