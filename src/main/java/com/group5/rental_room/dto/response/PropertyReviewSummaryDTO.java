package com.group5.rental_room.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PropertyReviewSummaryDTO {
    private Long propertyId;
    private Double averageRating;
    private Long totalReviews;
    private PaginatedResponse<ReviewResponseDTO> reviews;
}