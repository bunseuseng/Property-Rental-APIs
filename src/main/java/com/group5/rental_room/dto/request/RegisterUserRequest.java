package com.group5.rental_room.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterUserRequest {
    private String name;
    private String email;
    private String contactNumber;
    private String role;
    private String password;
}