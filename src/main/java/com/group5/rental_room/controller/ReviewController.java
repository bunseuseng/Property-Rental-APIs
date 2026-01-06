package com.group5.rental_room.controller;

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
}
