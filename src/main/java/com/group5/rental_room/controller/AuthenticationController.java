package com.group5.rental_room.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group5.rental_room.dto.response.APIsResponse;
import com.group5.rental_room.dto.request.RegisterUserRequest;
import com.group5.rental_room.dto.request.AuthenticationRequest;
import com.group5.rental_room.dto.request.RoleAssignRequest;
import com.group5.rental_room.entity.UserEntity;
import com.group5.rental_room.exception.BadRequestException;
import com.group5.rental_room.repositpory.UserRepository;
import com.group5.rental_room.service.AuthenticationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth-service")

public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationService authenticationService, UserRepository userReposiitory) {
        this.authenticationService = authenticationService;
        this.userRepository = userReposiitory;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity<APIsResponse<?>> register(@RequestBody RegisterUserRequest registerDto) {
        // ✅ Check duplicate email → throw BadRequestException (400)
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BadRequestException("User with email already exists"); // <<< NEW LINE
        }

        // ✅ Call service normally, no try/catch needed
        APIsResponse<?> response = authenticationService.register(registerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    @PostMapping("/assign-role")
    public ResponseEntity<APIsResponse<?>> assignRole(@RequestBody RoleAssignRequest request) {
        // ✅ service throws ResourceNotFoundException or BadRequestException as needed
        APIsResponse<?> response = authenticationService.assignRoleToUser(request);
        return ResponseEntity.ok(response); // 200 OK
    }


    @SuppressWarnings("rawtypes")
    @PostMapping("/authenticate")
    public ResponseEntity<APIsResponse<?>> authenticate(@RequestBody AuthenticationRequest request) {
        // ✅ service will throw UnauthorizedException for invalid credentials
        APIsResponse<?> response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response); // 200 OK
    }


}