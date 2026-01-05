package com.group5.rental_room.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private Long userId;
    private String gender;
    private String contactNumber;
    private List<String> role; // make role to response as array string "role": ["USER", "AGENT"],
    private String accessToken;
    private String refreshToken;
}