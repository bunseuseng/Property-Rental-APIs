package com.group5.rental_room.seeder;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.group5.rental_room.enums.enums;
import com.group5.rental_room.entity.Role;
import com.group5.rental_room.repositpory.RoleRepository;

import java.util.*;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        // 1. Define all roles including AGENT
        enums[] roleNames = new enums[] { enums.ADMIN, enums.USER, enums.AGENT };
        
        Map<enums, String> roleDescriptionMap = Map.of(
                enums.ADMIN, "Administrator role with full access",
                enums.USER, "Standard user role for browsing",
                enums.AGENT, "Agent role for listing properties"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            // 2. Use findByName (matching our earlier Role entity change)
        	Optional<Role> optionalRole = Optional.ofNullable(roleRepository.findByName(roleName));
            if (optionalRole.isEmpty()) {
                Role roleToCreate = new Role();
                roleToCreate.setName(roleName); // Use setName() 
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(roleToCreate);
                System.out.println("Seeded role: " + roleName);
            } else {
                System.out.println("Role already exists: " + roleName);
            }
        });
    }
}