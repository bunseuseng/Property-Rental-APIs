package com.group5.rental_room.controller;

import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {

 private final PropertyService propertyService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<PropertiesEntity> create(@RequestBody PropertyRequest request, Principal principal) {
        PropertiesEntity createdProperty = propertyService.createProperty(request, principal.getName());

        // Change .ok() to .status(HttpStatus.CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }





}
