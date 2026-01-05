package com.group5.rental_room.repositpory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group5.rental_room.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    UserEntity findByEmail (String email);

    boolean existsByEmail(String email);
    
    boolean findByPassword(String password);

}
