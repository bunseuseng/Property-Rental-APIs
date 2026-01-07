package com.group5.rental_room.dto.projection;

public interface PropertyReviewStats {
    Long getPropertyId();
    Double getAverageRating();
    Long getTotalReviews();
}
