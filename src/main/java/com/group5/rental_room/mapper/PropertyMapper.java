package com.group5.rental_room.mapper;

import com.group5.rental_room.dto.projection.PropertyReviewStats;
import com.group5.rental_room.dto.response.AgentDTO;
import com.group5.rental_room.dto.response.PropertyImageDTO;
import com.group5.rental_room.dto.response.PropertyResponseDTO;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.entity.PropertyImageEntity;
import com.group5.rental_room.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PropertyMapper {

    // ✅ Old method (1 argument) stays if you need it
    public static PropertyResponseDTO toResponse(PropertiesEntity propertiesEntity) {
        return buildPropertyResponse(propertiesEntity, null);
    }

    // ✅ New method (2 arguments) for averageRating & totalReviews
    public static PropertyResponseDTO toResponse(PropertiesEntity propertiesEntity, PropertyReviewStats stats) {
        return buildPropertyResponse(propertiesEntity, stats);
    }

    // 🔹 Common method to build the DTO
    private static PropertyResponseDTO buildPropertyResponse(PropertiesEntity propertiesEntity, PropertyReviewStats stats) {
        PropertyResponseDTO myProperty = new PropertyResponseDTO();
        myProperty.setId(propertiesEntity.getId());
        myProperty.setTitle(propertiesEntity.getTitle());
        myProperty.setDescription(propertiesEntity.getDescription());
        myProperty.setPrice(propertiesEntity.getPrice());
        myProperty.setLocation(propertiesEntity.getLocation());
        myProperty.setElectricityPrice(propertiesEntity.getElectricityPrice());
        myProperty.setWaterPrice(propertiesEntity.getWaterPrice());
        myProperty.setAgent(toAgentDTO(propertiesEntity.getAgent()));

        if (propertiesEntity.getImages() != null) {
            myProperty.setImages(
                    propertiesEntity.getImages()
                            .stream()
                            .map(PropertyMapper::toImageDTO)
                            .collect(Collectors.toList())
            );
        }

        // ✅ Add review stats if available
        if (stats != null) {
            Double avgRating = (stats.getAverageRating() != null)
                    ? Math.round(stats.getAverageRating() * 10.0) / 10.0
                    : 0.0;
            myProperty.setAverageRating(avgRating);
            myProperty.setTotalReviews(stats.getTotalReviews() != null ? stats.getTotalReviews() : 0L);
        } else {
            myProperty.setAverageRating(0.0);
            myProperty.setTotalReviews(0L);
        }

        return myProperty;
    }

    private static AgentDTO toAgentDTO(UserEntity userEntity) {
        AgentDTO agentDtoResponse = new AgentDTO();
        agentDtoResponse.setId(userEntity.getId());
        agentDtoResponse.setFullName(userEntity.getFullName());
        agentDtoResponse.setEmail(userEntity.getEmail());
        agentDtoResponse.setContactNumber(userEntity.getContactNumber());
        agentDtoResponse.setGender(userEntity.getGender());
        return agentDtoResponse;
    }

    private static PropertyImageDTO toImageDTO(PropertyImageEntity propertyImageEntity) {
        PropertyImageDTO imageDtoResponse = new PropertyImageDTO();
        imageDtoResponse.setId(propertyImageEntity.getId());
        imageDtoResponse.setImageUrl(propertyImageEntity.getImageUrl());
        return imageDtoResponse;
    }
}
