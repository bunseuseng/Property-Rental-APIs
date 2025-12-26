package com.group5.rental_room.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group5.rental_room.dto.response.APIsResponse;
import com.group5.rental_room.dto.request.RegisterUserRequest;
import com.group5.rental_room.dto.request.AuthenticationRequest;
import com.group5.rental_room.dto.request.RoleAssignRequest;
import com.group5.rental_room.entity.UserEntity;
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
    public ResponseEntity<APIsResponse> postMethodName(@RequestBody RegisterUserRequest registerDto) {
        try {
            UserEntity userModel = userRepository.findByEmail(registerDto.getEmail());
            
            if (userModel == null) {
                authenticationService.register(registerDto);
                
                APIsResponse apiResponse = APIsResponse.builder()
                        .message("User registered successfully!")
                        .statusCode(HttpStatus.CREATED.value()) // Use 201
                        .build();
                        
                return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
            } else {
                // A duplicate email is a Conflict (409), not a Server Error (500)
                APIsResponse apiResponse = APIsResponse.builder()
                        .message("User with email already exists")
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build();
                        
                return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
            }

        } catch (Exception ex) {
            APIsResponse apiResponse = APIsResponse.builder()
                    .message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
                    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    @PostMapping("/assign-role")
    public ResponseEntity<APIsResponse<UserEntity>> assignRole(@RequestBody RoleAssignRequest request) {
        try {
            APIsResponse<UserEntity> response = authenticationService.assignRoleToUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            // Return a parameterized ApiResponse even in case of error
            APIsResponse<UserEntity> errorResponse = APIsResponse.<UserEntity>builder()
                    .message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @SuppressWarnings("rawtypes")
	@PostMapping("/authenticate")
    public ResponseEntity<APIsResponse> postMethodName(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            APIsResponse res = authenticationService.authenticate(authenticationRequest);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            APIsResponse apiResponse = APIsResponse.builder().message("Failed: " + ex.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);

        }
    }

}