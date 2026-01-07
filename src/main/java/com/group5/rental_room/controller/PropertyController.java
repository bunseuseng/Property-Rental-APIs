package com.group5.rental_room.controller;

import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.dto.response.ListResponseDTO;
import com.group5.rental_room.dto.response.PropertyResponseDTO;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.mapper.PropertyMapper;
import com.group5.rental_room.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {

 private final PropertyService propertyService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<PropertyResponseDTO> create(@RequestBody PropertyRequest request, Principal principal) {
        // Create the property
        PropertiesEntity createdProperty = propertyService.createProperty(request, principal.getName());

        // Map entity -> DTO
        PropertyResponseDTO dto = PropertyMapper.toResponse(createdProperty);

        // Return with CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<PropertyResponseDTO> updatePropertyById (@PathVariable Long id,
    		@RequestBody PropertyRequest request, Principal principal) {
PropertyResponseDTO updateById = propertyService.updateProperty(id, request, principal.getName());
    	
    	return ResponseEntity.ok(updateById);
    }
    @GetMapping("/all")    //
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public HttpEntity<ListResponseDTO<PropertyResponseDTO>> getAllPropertiesForUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ListResponseDTO<PropertyResponseDTO> getAllProperties =
                propertyService.getAllProperties(page, size);

        return ResponseEntity.ok(getAllProperties);
    }

}
