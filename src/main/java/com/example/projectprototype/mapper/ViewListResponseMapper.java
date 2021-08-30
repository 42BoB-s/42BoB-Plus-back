package com.example.projectprototype.mapper;

import com.example.projectprototype.dto.ViewListResponseDto;
import com.example.projectprototype.entity.Room;

public interface ViewListResponseMapper {
	ViewListResponseDto toDto(Room room);
}
