package com.group5.rental_room.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.dto.response.*;
import com.group5.rental_room.exception.BadRequestException;
import com.group5.rental_room.exception.ConflictException;
import com.group5.rental_room.mapper.PropertyMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.group5.rental_room.dto.request.CreateReviewRequestDTO;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.entity.ReviewEntity;
import com.group5.rental_room.entity.UserEntity;
import com.group5.rental_room.exception.ResourceNotFoundException;
import com.group5.rental_room.repositpory.PropertyRepository;
import com.group5.rental_room.repositpory.ReviewRepository;
import com.group5.rental_room.repositpory.UserRepository;
import org.springframework.transaction.annotation.Transactional;

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
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("You can only rate between 1 and 5");
        }
    	UserEntity user = userRepository.findByEmail(userEmail);
    	PropertiesEntity property = propertyRepository.findById(request.getPropertyId())
    			.orElseThrow(()-> new ResourceNotFoundException("Property not found"));
    	
    	boolean alreadyReviewed = reviewRepository.existsByUserIdAndPropertyId(user.getId(), property.getId());
        // logic if that property has already reviewed
        if (alreadyReviewed) {
            throw new ConflictException("User already reviewed this property");
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

    @Override
    @Transactional(readOnly = true)
    public PropertyReviewSummaryDTO getAllReviewsForAProperty(
            Long propertyId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<ReviewEntity> myReviewPage =
                reviewRepository.findByPropertyId(propertyId, pageable);

        List<ReviewResponseDTO> reviews = myReviewPage.getContent()
                .stream()
                .map(reviewEntity -> ReviewResponseDTO.builder()
                        .reviewId(reviewEntity.getId())
                        .rating(reviewEntity.getRating())
                        .comment(reviewEntity.getComment())
                        .createdAt(reviewEntity.getCreatedAt())
                        .userId(reviewEntity.getUser().getId())
                        .fullName(reviewEntity.getUser().getFullName())
                        .build()
                )
                .toList();

        PaginatedResponse<ReviewResponseDTO> paginatedReviews =
                PaginatedResponse.<ReviewResponseDTO>builder()
                        .content(reviews)
                        .page(myReviewPage.getNumber())
                        .size(myReviewPage.getSize())
                        .totalElements(myReviewPage.getTotalElements())
                        .totalPages(myReviewPage.getTotalPages())
                        .last(myReviewPage.isLast())
                        .build();

        Double avgRating =
                reviewRepository.findAverageRatingByPropertyId(propertyId);

        Long totalReviews =
                reviewRepository.countByPropertyId(propertyId);

        return PropertyReviewSummaryDTO.builder()
                .propertyId(propertyId)
                .averageRating(
                        avgRating != null
                                ? Math.round(avgRating * 10.0) / 10.0   // 1 decimal place (4.6 and 2 decimal place 4.67)
                                : 0.0
                        /*
                         * This logic uses Ternary Operator which is the shorten way of If...Else
                         * If the average rating is not equal to null (meaning it exists),
                         * then take the average rating, multiply it by 10.0, round it to the
                         * nearest whole number, divided by 10.0.
                         * Otherwise (if it doesn't exist), use 0.0. (This is written by BongChang)
                         */
                )
                .totalReviews(totalReviews)
                .reviews(paginatedReviews)
                .build();
    }





}
