package com.example.projectprototype.mapper;

import com.example.projectprototype.GenericMapper;
import com.example.projectprototype.dto.ParticipantRequestDto;
import com.example.projectprototype.dto.RoomMenuRequestDto;
import com.example.projectprototype.entity.Participant;
import com.example.projectprototype.entity.RoomMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoomMenuRequestMapper extends GenericMapper<RoomMenuRequestDto, RoomMenu>{

}
