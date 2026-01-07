package com.group5.rental_room.repositpory;

import com.group5.rental_room.dto.projection.PropertyReviewStats;
import com.group5.rental_room.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
	  boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);

	  Page<ReviewEntity> findByPropertyId(Long propertyId, Pageable pageable);


     // Add aggregate queries in ReviewRepository
    @Query("""
        SELECT AVG(r.rating)
        FROM ReviewEntity r
        WHERE r.property.id = :propertyId
    """)
    Double findAverageRatingByPropertyId(@Param("propertyId") Long propertyId);

    @Query("""
        SELECT COUNT(r)
        FROM ReviewEntity r
        WHERE r.property.id = :propertyId
    """)
    Long countByPropertyId(@Param("propertyId") Long propertyId);

    @Query("""
    SELECT
        r.property.id AS propertyId,
        AVG(r.rating) AS averageRating,
        COUNT(r) AS totalReviews
    FROM ReviewEntity r
    GROUP BY r.property.id
""")
    List<PropertyReviewStats> findAllPropertyReviewStats();
    /*
    For each property
    calculate average rating
   and total number of reviews
     */

}