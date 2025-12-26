package com.group5.rental_room.dto.request;

import lombok.Data;

@Data
public class RoleAssignRequest {
	private Long userId;
	private Long roleId;
}
