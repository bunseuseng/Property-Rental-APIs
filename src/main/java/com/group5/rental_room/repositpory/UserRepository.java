package com.group5.rental_room.repositpory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group5.rental_room.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByUsername(String username);
     UserEntity findByEmail (String email);
	boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    boolean findByPassword(String password);

}
