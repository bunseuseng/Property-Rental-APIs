package com.group5.rental_room.repositpory;

import com.group5.rental_room.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
	  boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);

	  Page<ReviewEntity> findByPropertyId(Long propertyId, Pageable pageable);
}