package com.group5.rental_room.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data

public class CreateReviewRequestDTO {
    private Long propertyId;
    @Min(1)
    @Max(5)
    private int rating;
    private String comment;
}
