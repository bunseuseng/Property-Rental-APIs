package com.group5.rental_room.service;

import java.util.HashMap;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group5.rental_room.dto.response.APIsResponse;
import com.group5.rental_room.dto.request.AuthenticationRequest;
import com.group5.rental_room.dto.request.RegisterUserRequest;
import com.group5.rental_room.dto.request.RoleAssignRequest;
import com.group5.rental_room.enums.enums;
import com.group5.rental_room.entity.UserEntity;
import com.group5.rental_room.entity.Role;
import com.group5.rental_room.repositpory.RoleRepository;
import com.group5.rental_room.repositpory.UserRepository;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AuthenticationService {
    private final UserRepository userReposiitory;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userReposiitory, JwtService jwtService,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userReposiitory = userReposiitory;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
	@Transactional // This ensures the data is actually written to the DB

    public APIsResponse<UserEntity> register(RegisterUserRequest registerDto) {
        // 1. Create the user object
        UserEntity user = UserEntity.builder()
                .fullName(registerDto.getName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .phone(registerDto.getContactNumber())
                .status("ACTIVE")
                .roles(new HashSet<>()) 
                .build();

        // 2. Assign default USER role
        Role userRole = roleRepository.findByName(enums.USER);
        if (userRole != null) {
            user.getRoles().add(userRole);
        }

        // 3. Assign AGENT role if requested
        if (registerDto.getRole() != null && "AGENT".equalsIgnoreCase(registerDto.getRole())) {
            Role agentRole = roleRepository.findByName(enums.AGENT);
            if (agentRole != null) {
                user.getRoles().add(agentRole);
            }
        }

        userReposiitory.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), user);

        return APIsResponse.<UserEntity>builder()
                .message("User registered successfully")
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .statusCode(HttpStatus.CREATED.value())
                .build();
    }

    public APIsResponse<UserEntity> assignRoleToUser(RoleAssignRequest request) {
        UserEntity user = userReposiitory.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userReposiitory.save(user);

        return APIsResponse.<UserEntity>builder()
                .message("Role assigned successfully")
                .statusCode(200)
                .userId(user.getId())
                .role(user.getRoles().stream()
                        .map((Role r) -> r.getName().name()) 
                        .collect(Collectors.joining(", ")))
                .accessToken(jwtService.generateToken(user))
                .build();
    }
    
    public APIsResponse<UserEntity> authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()));

        UserEntity user = userReposiitory.findByEmail(authenticationRequest.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefresh(new HashMap<>(), user);

        String rolesString = user.getRoles().stream()
                .map((Role r) -> r.getName().name()) 
                .collect(Collectors.joining(","));

        return APIsResponse.<UserEntity>builder()
                .statusCode(HttpStatus.OK.value())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(rolesString) 
                .userId(user.getId())
                .build();
    }
}