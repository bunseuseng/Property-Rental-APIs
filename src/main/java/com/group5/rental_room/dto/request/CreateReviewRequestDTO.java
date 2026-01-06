package com.group5.rental_room.dto.request;

import lombok.Data;

@Data

public class CreateReviewRequestDTO {
    private Long propertyId;
    private int rating;
    private String comment;
}
