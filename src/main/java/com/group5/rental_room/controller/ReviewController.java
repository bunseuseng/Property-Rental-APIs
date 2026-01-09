package com.group5.rental_room.controller;

import com.group5.rental_room.dto.response.PaginatedResponse;
import com.group5.rental_room.dto.response.PropertyReviewSummaryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.group5.rental_room.dto.request.CreateReviewRequestDTO;
import com.group5.rental_room.dto.response.ReviewResponseDTO;
import com.group5.rental_room.service.ReviewService;

import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
	  private final ReviewService reviewService;


        // Create review
	    @PostMapping
	    @PreAuthorize("hasRole('USER')")

	    public ResponseEntity<ReviewResponseDTO> createReview (@Valid @RequestBody CreateReviewRequestDTO request, Authentication authentication){

            String name = authentication.getName();

	        return ResponseEntity.ok(reviewService.createReview(request, name));
	    }

    @GetMapping("/{propertyId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getAllReviewsByAProperty(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PropertyReviewSummaryDTO result =
                reviewService.getAllReviewsForAProperty(propertyId, page, size);

        PaginatedResponse<ReviewResponseDTO> paginatedReviews =
                result.getReviews();

        int currentPage = page + 1;
        // mapping
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("propertyId", result.getPropertyId());
        dataMap.put("averageRating", result.getAverageRating());
        dataMap.put("totalReviews", result.getTotalReviews());
        dataMap.put("reviews", paginatedReviews.getContent());

        // pagination
        Map<String, Object> paginationMap = new LinkedHashMap<>();
        paginationMap.put("page", currentPage);
        paginationMap.put("size", paginatedReviews.getSize());
        paginationMap.put("totalElements", paginatedReviews.getTotalElements());
        paginationMap.put("totalPages", paginatedReviews.getTotalPages());
        paginationMap.put("last", paginatedReviews.isLast());
        // response
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("success", true);
        responseMap.put("message", "Get all reviews from this property fetched successfully");
        responseMap.put("data", dataMap);
        responseMap.put("meta", paginationMap);

        return ResponseEntity.ok(responseMap);
    }


}
