package com.group5.rental_room.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "properties")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PropertiesEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	private String description;
	private Double price;
	private String location;
	private Double electricityPrice;
	private Double  waterPrice;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "agent-id", nullable= false )
	private UserEntity agent;
	@OneToMany(mappedBy = "property" , cascade = CascadeType.ALL, orphanRemoval = true )
	@JsonIgnoreProperties("property")
	private List<PropertyImageEntity> images = new ArrayList<>();
	
	

}
