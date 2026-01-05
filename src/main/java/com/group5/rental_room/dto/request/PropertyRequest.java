package com.group5.rental_room.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequest {
    private String title;
    private String description;
    private Double price;
    private String location;
    private Double electricityPrice;
    private Double  waterPrice;
    private List<String> imageUrls;
}