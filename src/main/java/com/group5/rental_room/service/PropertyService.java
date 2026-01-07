package com.group5.rental_room.service;

import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.dto.response.ApiResponseWithSuccess;
import com.group5.rental_room.dto.response.ListResponseDTO;
import com.group5.rental_room.dto.response.PropertyResponseDTO;

import com.group5.rental_room.entity.PropertiesEntity;

public interface PropertyService {
    PropertiesEntity createProperty (PropertyRequest propertyRequest, String email);
    PropertyResponseDTO updateProperty(Long id, PropertyRequest request, String email);
    ListResponseDTO<PropertyResponseDTO> getAllProperties(int page, int size);

    ListResponseDTO<PropertyResponseDTO> getPropertiesByAgent(String email, int page, int size);

    PropertyResponseDTO getPropertyById(Long id);


    ApiResponseWithSuccess deleteProperty(Long id, String email);
}
