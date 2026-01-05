package com.group5.rental_room.service;

import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.dto.response.PropertyResponseDTO;

import com.group5.rental_room.entity.PropertiesEntity;

public interface PropertyService {
    PropertiesEntity createProperty (PropertyRequest propertyRequest, String email);
    PropertyResponseDTO updateProperty(Long id, PropertyRequest request, String email);

}
