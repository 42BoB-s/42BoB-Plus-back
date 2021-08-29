package com.example.projectprototype.mapper;

import com.example.projectprototype.GenericMapper;
import com.example.projectprototype.dto.RoomCreateRequestDto;
import com.example.projectprototype.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomCreateRequestMapper extends GenericMapper <RoomCreateRequestDto, Room> {

}
