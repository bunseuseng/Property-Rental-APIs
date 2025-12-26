package com.group5.rental_room.repositpory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.group5.rental_room.enums.enums;
import com.group5.rental_room.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByName(enums name);}