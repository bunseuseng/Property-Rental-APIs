package com.group5.rental_room.repositpory;
import com.group5.rental_room.entity.PropertiesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PropertyRepository extends JpaRepository<PropertiesEntity, Long> {

    Page<PropertiesEntity> findByAgentId(Long agentId, Pageable pageable);
}

