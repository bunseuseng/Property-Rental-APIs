package com.group5.rental_room.service;

import java.util.HashMap;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.group5.rental_room.dto.response.AuthenticationResponse;
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
import com.group5.rental_room.exception.BadRequestException;
import com.group5.rental_room.exception.ResourceNotFoundException;
import com.group5.rental_room.exception.UnauthorizedException;
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
	    // 1. Check duplicate email
	    if (userReposiitory.existsByEmail(registerDto.getEmail())) {
	        throw new BadRequestException("User with email already exists"); // <<< NEW
	    }

	    // 2. Build user
	    UserEntity user = UserEntity.builder()
	            .fullName(registerDto.getFullName())
	            .email(registerDto.getEmail())
	            .password(passwordEncoder.encode(registerDto.getPassword()))
	            .contactNumber(registerDto.getContactNumber())
	            .gender(registerDto.getGender())
	            .status("ACTIVE")
	            .roles(new HashSet<>())
	            .build();

	    // 3. Assign roles
	    Role userRole = roleRepository.findByName(enums.USER);
	    if (userRole != null) user.getRoles().add(userRole);

	    if ("AGENT".equalsIgnoreCase(registerDto.getRole())) {
	        Role agentRole = roleRepository.findByName(enums.AGENT);
	        if (agentRole != null) user.getRoles().add(agentRole);
	    }

	    userReposiitory.save(user);

	    var jwtToken = jwtService.generateToken(user);
	    var refreshToken = jwtService.generateRefresh(user);

	    return APIsResponse.<UserEntity>builder()
	            .message("User registered successfully")
	            .statusCode(HttpStatus.CREATED.value())
	            .accessToken(jwtToken)
	            .refreshToken(refreshToken)
	            .build();
	}


	public APIsResponse<UserEntity> assignRoleToUser(RoleAssignRequest request) {
	    UserEntity user = userReposiitory.findById(request.getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found")); // <<< UPDATED

	    Role role = roleRepository.findById(request.getRoleId())
	            .orElseThrow(() -> new ResourceNotFoundException("Role not found")); // <<< UPDATED

	    if (user.getRoles().contains(role)) {
	        throw new BadRequestException("User already has this role assigned"); // <<< NEW
	    }

	    user.getRoles().add(role);
	    userReposiitory.save(user);

	    List<String> rolesList = user.getRoles().stream()
	            .map(r -> r.getName().name())
	            .collect(Collectors.toList());

	    return APIsResponse.<UserEntity>builder()
	            .message("Role assigned successfully")
	            .statusCode(HttpStatus.OK.value())
	            .userId(user.getId())
	            .role(rolesList)
	            .accessToken(jwtService.generateToken(user))
	            .build();
	}
    
	public APIsResponse<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {

	    try {
	        // ✅ Authenticate with Spring Security
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        authenticationRequest.getEmail(),
	                        authenticationRequest.getPassword()
	                )
	        );
	    } catch (Exception ex) {
	        // <<< NEW: If authentication fails, throw custom exception
	        throw new UnauthorizedException("Invalid email or password"); // 401
	    }

	    // ✅ Find user by email
	    UserEntity user = userReposiitory.findByEmail(authenticationRequest.getEmail());
	    if (user == null) {
	        throw new UnauthorizedException("Invalid email or password"); // <<< NEW
	    }

	    // ✅ Generate JWT tokens
	    var jwtToken = jwtService.generateToken(user);
	    var refreshToken = jwtService.generateRefresh(user);

	    // ✅ Map roles to list of strings
	    List<String> rolesList = user.getRoles().stream()
	            .map(r -> r.getName().name())
	            .collect(Collectors.toList());

	    // ✅ Build AuthenticationResponse
	    AuthenticationResponse response = AuthenticationResponse.builder()
	            .userId(user.getId())
	            .gender(user.getGender())
	            .contactNumber(user.getContactNumber())
	            .role(rolesList)
	            .accessToken(jwtToken)
	            .refreshToken(refreshToken)
	            .build();

	    // ✅ Wrap in APIsResponse
	    return APIsResponse.<AuthenticationResponse>builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Login successfully")
	            .data(response)
	            .build();
	}

}