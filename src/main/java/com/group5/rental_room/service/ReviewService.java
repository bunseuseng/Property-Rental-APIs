package com.group5.rental_room.service;

import com.group5.rental_room.dto.request.CreateReviewRequestDTO;
import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.dto.response.*;

public interface ReviewService {

    ReviewResponseDTO createReview(CreateReviewRequestDTO request, String userEmail);
    PropertyReviewSummaryDTO
    getAllReviewsForAProperty(Long propertyId, int page, int size);


}
