package com.group5.rental_room.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private Double electricityPrice;
    private Double  waterPrice; 
    private String location;
    private Double averageRating;
    private Long totalReviews;
    private AgentDTO agent; 
    private List<PropertyImageDTO> images;
}