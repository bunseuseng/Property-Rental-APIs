package com.group5.rental_room.service;

import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.entity.PropertyImageEntity;
import com.group5.rental_room.entity.UserEntity;
import com.group5.rental_room.repositpory.PropertyRepository;
import com.group5.rental_room.repositpory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    @Override
    public PropertiesEntity createProperty(PropertyRequest propertyRequest, String email) {
        UserEntity agent = userRepository.findByEmail(email);

        // Build property
        PropertiesEntity MyProperty = PropertiesEntity.builder()
                .title(propertyRequest.getTitle())
                .description(propertyRequest.getDescription())
                .price(propertyRequest.getPrice())
                .location(propertyRequest.getLocation())
                .electricityPrice(propertyRequest.getElectricityPrice())
                .waterPrice(propertyRequest.getWaterPrice())
                .agent(agent)
                .images( new ArrayList<>())
                .build();

            // Check if images present n the DTO
        if(propertyRequest.getImageUrls() != null){
            for ( String url : propertyRequest.getImageUrls()){
                PropertyImageEntity img = PropertyImageEntity.builder()
                        .imageUrl(url)
                        .property(MyProperty)
                        .build();
                MyProperty.getImages() .add(img);
            }
        }


        return propertyRepository.save(MyProperty);
    }
}
