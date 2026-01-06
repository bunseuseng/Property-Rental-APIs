package com.group5.rental_room.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.group5.rental_room.dto.request.CreateReviewRequestDTO;
import com.group5.rental_room.dto.response.ReviewResponseDTO;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.entity.ReviewEntity;
import com.group5.rental_room.entity.UserEntity;
import com.group5.rental_room.exception.ResourceNotFoundException;
import com.group5.rental_room.repositpory.PropertyRepository;
import com.group5.rental_room.repositpory.ReviewRepository;
import com.group5.rental_room.repositpory.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    // Constructor injections
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
//    private final CommentRepository commentRepository;
    
    @Override
    public ReviewResponseDTO createReview(CreateReviewRequestDTO request, String userEmail) {
    	UserEntity user = userRepository.findByEmail(userEmail);
    	PropertiesEntity property = propertyRepository.findById(request.getPropertyId())
    			.orElseThrow(()-> new ResourceNotFoundException("Property not found"));
    	
    	boolean alreadyReviewed = reviewRepository.existsByUserIdAndPropertyId(user.getId(), property.getId());
        // logic if that property has already reviewed
        if(alreadyReviewed){
            throw new ResourceNotFoundException("User already review this property");

        }
        
     // instance to create object by using builder
        ReviewEntity review = ReviewEntity.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .user(user)
                .property(property)
                .createdAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
        
        return ReviewResponseDTO.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .userId(user.getId())
                .fullName(user.getFullName())
                .build();

    }
    
}
