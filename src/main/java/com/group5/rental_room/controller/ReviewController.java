package com.group5.rental_room.controller;

import com.group5.rental_room.dto.response.PaginatedResponse;
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
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
	  private final ReviewService reviewService;

	    @PostMapping("/create")
	    @PreAuthorize("hasRole('USER')")

	    public ResponseEntity<ReviewResponseDTO> createReview (  @RequestBody CreateReviewRequestDTO request,
	                                                             Authentication authentication){
	      String email = authentication.getName();

	        return ResponseEntity.ok(reviewService.createReview(request, email));
	    }

	@GetMapping("/properties/{propertyId}/reviews")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Map<String, Object>> getAllReviewsByAProperty(
			@PathVariable Long propertyId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		PaginatedResponse<ReviewResponseDTO> paginatedReviews = reviewService.getAllReviewsForAProperty(propertyId, page, size);

		int currentPage = page + 1;
		Map<String, Object> dataMap = new LinkedHashMap<>();
		dataMap.put("propertyId", propertyId);
		dataMap.put("reviews", paginatedReviews.getContent());

		Map<String, Object> paginationMap = new LinkedHashMap<>();
		paginationMap.put("page", currentPage);
		paginationMap.put("size", paginatedReviews.getSize());
		paginationMap.put("totalElements", paginatedReviews.getTotalElements());
		paginationMap.put("totalPages", paginatedReviews.getTotalPages());
		paginationMap.put("last", paginatedReviews.isLast());

		Map<String, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("success", true);
		responseMap.put("message", "Get all reviews from this property fetched successfully");
		responseMap.put("data", dataMap);
		responseMap.put("meta", paginationMap);

		return ResponseEntity.ok(responseMap);
	}



}
