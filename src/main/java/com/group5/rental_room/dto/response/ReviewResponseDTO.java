package com.group5.rental_room.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ReviewResponseDTO {

    private Long reviewId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private Long userId;
    private String fullName;
}
