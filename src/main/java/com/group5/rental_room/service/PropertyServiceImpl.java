package com.group5.rental_room.service;

import com.group5.rental_room.dto.projection.PropertyReviewStats;
import com.group5.rental_room.dto.request.PropertyRequest;
import com.group5.rental_room.dto.response.ApiResponseWithSuccess;
import com.group5.rental_room.dto.response.ListResponseDTO;
import com.group5.rental_room.dto.response.PropertyImageDTO;
import com.group5.rental_room.dto.response.PropertyResponseDTO;
import com.group5.rental_room.entity.PropertiesEntity;
import com.group5.rental_room.entity.PropertyImageEntity;
import com.group5.rental_room.entity.UserEntity;
import com.group5.rental_room.repositpory.PropertyRepository;
import com.group5.rental_room.repositpory.ReviewRepository;
import com.group5.rental_room.repositpory.UserRepository;
import com.group5.rental_room.exception.ResourceNotFoundException;
import com.group5.rental_room.mapper.PropertyMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
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
	@Override
	public PropertyResponseDTO updateProperty(Long id, PropertyRequest request, String email) {
		// TODO Auto-generated method stub

		PropertiesEntity property = propertyRepository.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Property not found with this id" + id));
		if(!property.getAgent().getEmail().equals(email)) {
      	  throw new RuntimeException("You are not allowed to update this property");
		}
		property.setTitle(request.getTitle());
		property.setDescription(request.getDescription());
		property.setPrice(request.getPrice());
		property.setElectricityPrice(request.getElectricityPrice());
		property.setWaterPrice(request.getWaterPrice());
		property.setLocation(request.getLocation());
		 property.getImages().clear();// orphanRemoval = true → old images deleted
         request.getImageUrls().forEach(url ->
             property.getImages().add(
                 PropertyImageEntity.builder()
                     .imageUrl(url)
                     .property(property)
                     .build()
             )
       		  );

         PropertiesEntity updated = propertyRepository.save(property);
         return PropertyMapper.toResponse(updated);	}


    @Override
    public ListResponseDTO<PropertyResponseDTO> getAllProperties(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PropertiesEntity> propertyPage = propertyRepository.findAll(pageable);

        // ✅ 1️⃣ Fetch all review stats in ONE query
        Map<Long, PropertyReviewStats> reviewStatsMap =
                reviewRepository.findAllPropertyReviewStats()
                        .stream()
                        .collect(Collectors.toMap(
                                PropertyReviewStats::getPropertyId,
                                stats -> stats
                        ));

        // ✅ 2️⃣ Map properties → DTO including review stats
        List<PropertyResponseDTO> dtoList = propertyPage.getContent()
                .stream()
                .map(property -> PropertyMapper.toResponse(property, reviewStatsMap.get(property.getId())))
                .collect(Collectors.toList());

        int currentPage = propertyPage.getNumber() + 1;
        int pageSize = propertyPage.getSize();
        int totalPages = propertyPage.getTotalPages();
        long totalItems = propertyPage.getTotalElements();
        boolean hasNext = propertyPage.hasNext();
        boolean hasPrevious = propertyPage.hasPrevious();

        return new ListResponseDTO<>(
                true,
                "Properties fetched successfully",
                dtoList,
                currentPage,
                pageSize,
                totalPages,
                totalItems,
                hasNext,
                hasPrevious
        );
    }

    @Override
    public ListResponseDTO<PropertyResponseDTO> getPropertiesByAgent(String email, int page, int size) {
        return null;
    }

    @Override
    public PropertyResponseDTO getPropertyById(Long id) {
        return null;
    }

    @Override
    public ApiResponseWithSuccess deleteProperty(Long id, String email) {
        return null;
    }
}
