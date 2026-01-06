package com.group5.rental_room.mapper;

import com.group5.rental_room.dto.response.ReviewResponseDTO;
import com.group5.rental_room.entity.ReviewEntity;

public class ReviewMapper {

    public static ReviewResponseDTO toDTO (ReviewEntity reviewEntity){
        return ReviewResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .rating(reviewEntity.getRating())
                .initialComment(reviewEntity.getComment())
                .createdAt(reviewEntity.getCreatedAt())
                .userId(reviewEntity.getUser().getId())
                .fullName(reviewEntity.getUser().getFullName())
                .build();


    }


}
