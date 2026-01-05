package com.group5.rental_room.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterUserRequest {
    private String fullName;
    private String email;
    private String contactNumber;
    private String role;
    private String gender;
    private String password;
}