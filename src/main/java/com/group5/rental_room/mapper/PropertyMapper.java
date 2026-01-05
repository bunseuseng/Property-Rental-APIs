package com.group5.rental_room.mapper;

import com.group5.rental_room.dto.response.AgentDTO;
import com.group5.rental_room.dto.response.PropertyImageDTO;
import com.group5.rental_room.dto.response.PropertyResponseDTO;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.entity.PropertyImageEntity;
import com.group5.rental_room.entity.UserEntity;

import java.util.stream.Collectors;

public class PropertyMapper {

    public static PropertyResponseDTO toResponse (PropertiesEntity propertiesEntity){

        PropertyResponseDTO  myProperty = new PropertyResponseDTO();
        myProperty.setId(propertiesEntity.getId());
        myProperty.setTitle(propertiesEntity.getTitle());
        myProperty.setDescription(propertiesEntity.getDescription());
        myProperty.setPrice(propertiesEntity.getPrice());
        myProperty.setLocation(propertiesEntity.getLocation());
        myProperty.setElectricityPrice(propertiesEntity.getElectricityPrice());
        myProperty.setWaterPrice(propertiesEntity.getWaterPrice());
        myProperty.setAgent(toAgentDTO(propertiesEntity.getAgent()));

        if(propertiesEntity.getImages() !=null){
            myProperty.setImages(
                    propertiesEntity.getImages()
                            .stream()
                            .map(PropertyMapper::toImageDTO)
                            .collect(Collectors.toList())
            );
        }



        return myProperty;
    }

    private static AgentDTO toAgentDTO (UserEntity userEntity){
        AgentDTO agentDtoResponse = new AgentDTO();
        agentDtoResponse.setId(userEntity.getId());
        agentDtoResponse.setName(userEntity.getFullName());
        agentDtoResponse.setEmail(userEntity.getEmail());
        return agentDtoResponse;

    }

    private static PropertyImageDTO toImageDTO (PropertyImageEntity propertyImageEntity){
        PropertyImageDTO imageDtoResponse = new PropertyImageDTO();
        imageDtoResponse.setId(propertyImageEntity.getId());
        imageDtoResponse.setImageUrl(propertyImageEntity.getImageUrl());


        return imageDtoResponse;
    }

}
