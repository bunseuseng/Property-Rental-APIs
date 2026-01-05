package com.group5.rental_room.entity;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // Explicitly mapping to your 'user' table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "phone")
    private String contactNumber;
    
    @JsonIgnore
    private String password;
    @Column(name = "gender")
    private String gender;
    private String status; // e.g., "ACTIVE", "INACTIVE"

    // Many-to-Many relationship with the 'role' table via 'user_role' join table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> {
                    // 1. role.getName() returns the 'enums' object
                    // 2. .name() converts the Enum to a String (e.g., "AGENT")
                    // 3. .toUpperCase() ensures it's in the correct format
                    String roleString = role.getName().name().toUpperCase();
                    return new SimpleGrantedAuthority("ROLE_" + roleString);
                })
                .collect(Collectors.toList());
    }
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}